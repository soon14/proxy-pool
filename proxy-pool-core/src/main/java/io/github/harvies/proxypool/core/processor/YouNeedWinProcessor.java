package io.github.harvies.proxypool.core.processor;

import io.github.harvies.proxypool.api.domain.Proxy;
import io.github.harvies.proxypool.api.domain.ProxyOrigin;
import io.github.harvies.proxypool.core.factory.ProcessorFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.RequestsDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * @author harvies
 */
@Component
@Slf4j
public class YouNeedWinProcessor extends AbstractProcessor implements PageProcessor, InitializingBean {

    @Scheduled(fixedDelay = 1000 * 60)
    @Override
    public void crawl() {
        super.crawl();
    }


    @Override
    public void start() {
        spider = Spider.create(new YouNeedWinProcessor())
                .addUrl("https://github.com/dxxzst/free-proxy-list/blob/master/README.md")
                .setDownloader(new RequestsDownloader())
                .addPipeline(proxyMongoPipeline);
        spider.run();
    }

    @Override
    public ProxyOrigin getOrigin() {
        return ProxyOrigin.of(1, "YouNeedWin");
    }

    @Override
    public void process(Page page) {
        List<Selectable> nodes = page.getHtml().xpath("//*[@id=\"readme\"]/article/table/tbody/tr").nodes();
        nodes.forEach(selectable -> {
            List<String> all = selectable.xpath("//td/text()").all();
            Proxy.ProxyBuilder proxyBuilder = Proxy.builder()
                    .ip(all.get(0))
                    .port(Integer.valueOf(all.get(1)))
                    .protocol(all.get(2))
                    .origin(getOrigin().getId());
            String id = DigestUtils.md5Hex(all.get(0) + all.get(1));
            page.putField(id, proxyBuilder.build());
        });
    }

    @Override
    public Site getSite() {
        return Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(1000).setTimeOut(10000);
    }

    @Override
    public void afterPropertiesSet() {
        ProcessorFactory.register(this);
    }
}
