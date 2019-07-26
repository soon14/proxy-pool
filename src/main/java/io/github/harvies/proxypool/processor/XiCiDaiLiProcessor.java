package io.github.harvies.proxypool.processor;

import io.github.harvies.proxypool.domain.Proxy;
import io.github.harvies.proxypool.domain.ProxyOrigin;
import io.github.harvies.proxypool.factory.ProcessorFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.RequestsDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author harvies
 */
@Component
@Slf4j
public class XiCiDaiLiProcessor extends AbstractProcessor implements PageProcessor, InitializingBean {

    @Scheduled(fixedDelay = 1000 * 60)
    @Override
    public void crawl() {
        super.crawl();
    }

    @Override
    public void start() {
        spider = Spider.create(new XiCiDaiLiProcessor())
                .addUrl("https://www.xicidaili.com/nn/")
                .addUrl("https://www.xicidaili.com/nt/")
                .addUrl("https://www.xicidaili.com/wn/")
                .addUrl("https://www.xicidaili.com/wt/")
                .addUrl("https://www.xicidaili.com/qq/")
//                .setScheduler(new RedisScheduler("localhost"))
                .addPipeline(proxyMongoPipeline);
//        HttpClientDownloader downloader = new HttpClientDownloader();
        RequestsDownloader downloader = new RequestsDownloader();
        downloader.setProxyProvider(dynamicProxyProvider);
        spider.setDownloader(downloader);
        spider.run();
    }

    @Override
    public ProxyOrigin getOrigin() {
        return ProxyOrigin.of(2, "西刺");
    }

    @Override
    public void process(Page page) {
        List<Selectable> nodes = page.getHtml().xpath("//*[@id=\"ip_list\"]/tbody/tr").nodes();
        for (Selectable selectable :
                nodes) {
            try {
                List<String> all = selectable.xpath("//td/text()").all();
                if (all.size() == 0) {
                    continue;
                }
                Date verifyTime = null;
                try {
                    verifyTime = DateUtils.parseDate(all.get(9), "yy-MM-dd HH:mm");
                } catch (ParseException e) {
                    log.debug("ParseException selectable:{}", selectable, e);
                }
                if (verifyTime == null) {
                    continue;
                }
                /**
                 * 校验时间小于7天
                 */
                Date date = Date.from(Instant.now());
                if (DateUtils.addDays(verifyTime, 7).before(date)) {
                    break;
                }
                Proxy.ProxyBuilder proxyBuilder = Proxy.builder()
                        .ip(all.get(1))
                        .port(Integer.valueOf(all.get(2)))
                        .protocol(all.get(5).toLowerCase())
                        .origin(getOrigin().getId());
                //QQ代理就是socks代理
                if (Objects.equals("QQ代理", all.get(5))) {
                    proxyBuilder.protocol("socks4");
                }
                String id = DigestUtils.md5Hex(all.get(1) + all.get(2));
                page.putField(id, proxyBuilder.build());
            } catch (Exception e) {
                log.info("Exception selectable:{}", selectable, e);
            }
        }
        /**
         * 页数小于99
         */
        page.addTargetRequests(page.getHtml().links().regex("https://www.xicidaili.com/(?:nt)?(?:nn)?(?:qq)?(?:wn)?(?:wt)?/[1-9]{1,2}$").all());
    }

    @Override
    public Site getSite() {
        return Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(10000).setTimeOut(10000).setUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
    }

    @Override
    public void afterPropertiesSet() {
        ProcessorFactory.register(this);
    }
}
