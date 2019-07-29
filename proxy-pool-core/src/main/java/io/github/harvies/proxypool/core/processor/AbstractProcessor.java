package io.github.harvies.proxypool.core.processor;

import io.github.harvies.proxypool.core.config.Config;
import io.github.harvies.proxypool.core.pipeline.ProxyMongoPipeline;
import io.github.harvies.proxypool.core.proxy.DynamicProxyProvider;
import io.github.harvies.proxypool.core.proxy.GoogleDynamicProxyProvider;
import io.github.harvies.proxypool.api.domain.ProxyOrigin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Spider;

import java.util.Objects;

/**
 * @author harvies
 */
@Slf4j
public abstract class AbstractProcessor {

    @Autowired
    protected ProxyMongoPipeline proxyMongoPipeline;
    @Autowired
    protected DynamicProxyProvider dynamicProxyProvider;
    @Autowired
    protected GoogleDynamicProxyProvider googleDynamicProxyProvider;

    protected Spider spider;

    @Autowired
    private Config config;

    /**
     * 该爬虫是否启用
     *
     * @return
     */
    public boolean isEnable() {
        return config.getEnableProcessorIdList().contains(this.getOrigin().getId());
    }

    protected void crawl() {
        if (isEnable()) {
            long currentTimeMillis = System.currentTimeMillis();
            log.warn("crawl {}  proxy begin!", getOrigin().getName());
            start();
            log.warn("crawl {}  proxy end!耗时:{}ms", getOrigin().getName(), System.currentTimeMillis() - currentTimeMillis);
        }
    }

    protected abstract void start();

    public void stop() {
        spider.stop();
    }

    /**
     * 来源
     *
     * @return
     */
    public abstract ProxyOrigin getOrigin();


    public boolean isStop() {
        return Objects.equals(Spider.Status.Stopped.name(), spider.getStatus().name());
    }
}
