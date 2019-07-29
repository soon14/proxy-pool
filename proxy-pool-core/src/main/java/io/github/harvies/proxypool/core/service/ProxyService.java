package io.github.harvies.proxypool.core.service;

import io.github.harvies.proxypool.api.domain.Proxy;

import java.util.List;

/**
 * @author harvies
 */
public interface ProxyService {

    /**
     * 查询将要检测的代理列表
     *
     * @return
     */
    List<Proxy> findWillBeCheckedProxyList();

    Proxy save(Proxy proxy);

    long clean();
}
