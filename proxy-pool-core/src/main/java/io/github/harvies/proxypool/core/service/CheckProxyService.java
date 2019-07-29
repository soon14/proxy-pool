package io.github.harvies.proxypool.core.service;

import io.github.harvies.proxypool.api.domain.Proxy;
import io.github.harvies.proxypool.api.domain.ProxyResult;

/**
 * @author harvies
 */
public interface CheckProxyService {
    ProxyResult check(Proxy proxy);
}
