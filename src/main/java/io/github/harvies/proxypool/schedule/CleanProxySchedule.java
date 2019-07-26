package io.github.harvies.proxypool.schedule;

import io.github.harvies.proxypool.service.ProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author harvies
 */
@Component
@Slf4j
public class CleanProxySchedule {

    @Autowired
    private ProxyService proxyService;

    @Scheduled(fixedDelayString = "${proxy-pool.cleanProxy.fixedDelay}")
    void init() {
        long currentTimeMillis = System.currentTimeMillis();
        log.warn("{} clean proxy begin!", Thread.currentThread().getName());
        String oldThreadName = Thread.currentThread().getName();
        try {
            Thread.currentThread().setName("CleanProxy-" + oldThreadName);
            long clean = proxyService.clean();
            log.warn("清理代理条数:{}条", clean);
        } finally {
            Thread.currentThread().setName(oldThreadName);
        }
        log.warn("{} clean proxy end!耗时:{}ms", Thread.currentThread().getName(), System.currentTimeMillis() - currentTimeMillis);

    }
}
