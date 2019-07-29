package io.github.harvies.proxypool.core.processor;

import com.google.common.base.Splitter;
import io.github.harvies.proxypool.core.util.ProtocolUtils;
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
public class XiLaDaiLiProcessor extends AbstractProcessor implements PageProcessor, InitializingBean {

    @Scheduled(fixedDelay = 1000 * 60 * 10)
    @Override
    public void crawl() {
        super.crawl();
    }


    @Override
    public void start() {
        spider = Spider.create(new XiLaDaiLiProcessor())

                .addPipeline(proxyMongoPipeline);
        RequestsDownloader downloader = new RequestsDownloader();
        downloader.setProxyProvider(dynamicProxyProvider);
        spider.setDownloader(downloader);
        for (int i = 1; i <= 1000; i++) {
            spider.addUrl("http://www.xiladaili.com/gaoni/" + i);
            spider.addUrl("http://www.xiladaili.com/http/" + i);
            spider.addUrl("http://www.xiladaili.com/https/" + i);
            spider.addUrl("http://www.xiladaili.com/putong/" + i);
        }

        spider.run();
    }

    @Override
    public ProxyOrigin getOrigin() {
        return ProxyOrigin.of(4, "西拉代理");
    }

    @Override
    public void process(Page page) {
        List<Selectable> nodes = page.getHtml().xpath("/html/body/div/div[3]/div[2]/table/tbody/tr").nodes();
        nodes.forEach(selectable -> {
            List<String> all = selectable.xpath("//td/text()").all();
            List<String> ipPort = Splitter.on(':').splitToList(all.get(0));
            String ip = ipPort.get(0);
            String port = ipPort.get(1);
            String protocolStr = all.get(1);
            String protocol = ProtocolUtils.getProtocol(protocolStr);
            Proxy.ProxyBuilder proxyBuilder = Proxy.builder()
                    .ip(ip)
                    .port(Integer.valueOf(port))
                    .protocol(protocol)
                    .origin(getOrigin().getId());
            String id = DigestUtils.md5Hex(ip + port);
            page.putField(id, proxyBuilder.build());
        });
    }

    @Override
    public Site getSite() {
        return Site.me().setCycleRetryTimes(3).setRetryTimes(3).setSleepTime(1000).setTimeOut(3000);
    }

    @Override
    public void afterPropertiesSet() {
        ProcessorFactory.register(this);
    }
}
