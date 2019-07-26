package io.github.harvies.proxypool.service;

import io.github.harvies.proxypool.domain.Proxy;
import io.github.harvies.proxypool.domain.ProxyResult;

/**
 * @author harvies
 */
public interface CheckProxyService {
    ProxyResult check(Proxy proxy);
}
