package io.github.harvies.proxypool.service;

import io.github.harvies.proxypool.BaseTest;
import io.github.harvies.proxypool.api.domain.Proxy;
import io.github.harvies.proxypool.web.query.ProxyQuery;
import io.github.harvies.proxypool.web.service.WebProxyService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class ProxyServiceTest extends BaseTest {
    @Autowired
    private WebProxyService proxyService;

    @Test
    public void availableNum() {
        long availableNum = proxyService.count(ProxyQuery.builder().delayLte(1000).build());
        log.warn("count:{}", availableNum);
    }

    @Test
    public void availableList() {
        List<Proxy> availableList = proxyService.list(ProxyQuery.builder().delayLte(1000).build());
        availableList.forEach(System.out::println);
    }
}
