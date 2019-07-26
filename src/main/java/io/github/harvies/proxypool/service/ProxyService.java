package io.github.harvies.proxypool.service;

import io.github.harvies.proxypool.domain.Proxy;
import io.github.harvies.proxypool.query.ProxyQuery;

import java.util.List;

/**
 * @author harvies
 */
public interface ProxyService {
    /**
     * 可用代理数
     *
     * @param proxyQuery
     * @return
     */
    long count(ProxyQuery proxyQuery);

    /**
     * 可用代理列表
     *
     * @param proxyQuery
     * @return
     */
    List<Proxy> list(ProxyQuery proxyQuery);

    /**
     * 查询将要检测的代理列表
     *
     * @return
     */
    List<Proxy> findWillBeCheckedProxyList();

    /**
     * 随机返回一个可用代理
     *
     * @param proxyQuery
     * @return
     */
    Proxy get(ProxyQuery proxyQuery);

    Proxy save(Proxy proxy);

    Boolean deleteById(String id);

    long clean();

    /**
     * 稳定性减1
     *
     * @param proxy
     * @return
     */
    long decrementStability(Proxy proxy);

    /**
     * 稳定性加1
     *
     * @param proxy
     * @return
     */
    long incrementStability(Proxy proxy);
}
