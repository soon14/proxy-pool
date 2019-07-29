package io.github.harvies.proxypool.web.service;

import io.github.harvies.proxypool.api.domain.Proxy;
import io.github.harvies.proxypool.web.query.ProxyQuery;

import java.util.List;

/**
 * @author harvies
 */
public interface WebProxyService {
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

    Boolean deleteById(String id);


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
