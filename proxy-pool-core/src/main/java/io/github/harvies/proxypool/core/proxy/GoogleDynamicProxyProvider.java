package io.github.harvies.proxypool.core.proxy;

import lombok.extern.slf4j.Slf4j;
import net.dongliu.requests.Requests;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;


/**
 * @author harvies
 */
@Slf4j
@Component
public class GoogleDynamicProxyProvider extends DynamicProxyProvider implements ProxyProvider {


    @Override
    public Proxy getProxy(Task task) {
        try {
            io.github.harvies.proxypool.api.domain.Proxy proxy = Requests.get(host + port + "/proxy/get?google=true&delayLte=1000000&stabilityGt=0").send().readToJson(io.github.harvies.proxypool.api.domain.Proxy.class);
            return new Proxy(proxy.getIp(), proxy.getPort(), proxy.getType());
        } catch (Exception e) {
            return null;
        }
    }
}
