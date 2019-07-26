package io.github.harvies.proxypool.processor;

import io.github.harvies.proxypool.domain.Proxy;
import io.github.harvies.proxypool.domain.ProxyOrigin;
import io.github.harvies.proxypool.factory.ProcessorFactory;
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
import java.util.Objects;

/**
 * @author harvies
 */
@Component
@Slf4j
public class FreeProxyListNetProcessor extends AbstractProcessor implements PageProcessor, InitializingBean {

    @Scheduled(fixedDelay = 1000 * 60)
    @Override
    public void crawl() {
        super.crawl();
    }


    @Override
    public void start() {
        RequestsDownloader downloader = new RequestsDownloader();
        downloader.setProxyProvider(googleDynamicProxyProvider);
        spider = Spider.create(new FreeProxyListNetProcessor())
                .addUrl("https://free-proxy-list.net/")
                .addUrl("https://www.us-proxy.org/")
                .addUrl("https://free-proxy-list.net/uk-proxy.html")
                .addUrl("https://www.sslproxies.org/")
                .addUrl("https://free-proxy-list.net/anonymous-proxy.html")
                .addUrl("https://www.socks-proxy.net/")
                .setDownloader(downloader)
                .addPipeline(proxyMongoPipeline);
        /**
         * 每个爬虫使用单线程
         */
        spider.run();
    }

    @Override
    public ProxyOrigin getOrigin() {
        return ProxyOrigin.of(3, "free-proxy-list");
    }

    @Override
    public void process(Page page) {
        List<Selectable> nodes = page.getHtml().xpath("//*[@id=\"proxylisttable\"]/tbody/tr").nodes();
        nodes.forEach(selectable -> {
            List<String> all = selectable.xpath("//td/text()").all();
            Proxy.ProxyBuilder proxyBuilder = Proxy.builder()
                    .ip(all.get(0))
                    .port(Integer.valueOf(all.get(1)))
                    .origin(getOrigin().getId());
            proxyBuilder.protocol(Objects.equals(all.get(6), "yes") ? "https" : "http");
            String aCase = all.get(4).toLowerCase();
            if (aCase.contains("socks")) {
                proxyBuilder.protocol(aCase);
            }
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
