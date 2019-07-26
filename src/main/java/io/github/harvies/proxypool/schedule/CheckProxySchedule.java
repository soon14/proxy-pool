package io.github.harvies.proxypool.schedule;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.harvies.proxypool.domain.Proxy;
import io.github.harvies.proxypool.service.CheckProxyService;
import io.github.harvies.proxypool.service.ProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author harvies
 */
@Component
@Slf4j
@ConditionalOnProperty(prefix = "proxy-pool", name = "enableCheck")
public class CheckProxySchedule {

    private static ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder()
            .setDaemon(false).setNameFormat("crawl proxy-thread-pool-%d");
    private static ExecutorService executorService = new ThreadPoolExecutor(100, 100, 0,
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(1000), threadFactoryBuilder.build());


    @Autowired
    private ProxyService proxyService;

    @Autowired
    private CheckProxyService checkProxyService;

    @Scheduled(fixedDelayString = "${proxy-pool.checkProxy.fixedDelay}")
    void init() {
        long currentTimeMillis = System.currentTimeMillis();
        log.warn("{} check proxy begin!", Thread.currentThread().getName());

        List<Proxy> proxyList = proxyService.findWillBeCheckedProxyList();
        log.warn("{} check proxy size:{}!", Thread.currentThread().getName(), proxyList.size());
        CountDownLatch countDownLatch = new CountDownLatch(proxyList.size());
        for (Proxy proxy :
                proxyList) {
            executorService.execute(() -> {
                try {
                    checkProxyService.check(proxy);
                } catch (Exception e) {
                    log.info("", e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.warn("InterruptedException", e);
        }
        log.warn("{} check proxy end!耗时:{}ms", Thread.currentThread().getName(), System.currentTimeMillis() - currentTimeMillis);

    }


}

