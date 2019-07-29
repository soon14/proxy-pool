package io.github.harvies.proxypool.core.proxy;

import lombok.extern.slf4j.Slf4j;
import net.dongliu.requests.Requests;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;


/**
 * @author harvies
 */
@Slf4j
@Component
public class DynamicProxyProvider implements ProxyProvider {
    @Value("${server.port}")
    protected int port;

    protected static String host = "http://localhost:";

    @Override
    public void returnProxy(Proxy proxy, Page page, Task task) {
        log.warn("url:{} isDownloadSuccess:{} proxy:{}", page.getUrl(), page.isDownloadSuccess(), proxy);
        if (page.isDownloadSuccess()) {
            Requests.get(host + port + "/proxy/incrementStability?ip=" + proxy.getHost() + "&port=" + proxy.getPort()).send().close();
        } else {
            Requests.get(host + port + "/proxy/decrementStability?ip=" + proxy.getHost() + "&port=" + proxy.getPort()).send().close();
        }
    }

    @Override
    public Proxy getProxy(Task task) {
        try {
            io.github.harvies.proxypool.api.domain.Proxy proxy = Requests.get(host + port + "/proxy/get").send().readToJson(io.github.harvies.proxypool.api.domain.Proxy.class);
            return new Proxy(proxy.getIp(), proxy.getPort(), proxy.getType());
        } catch (Exception e) {
            return null;
        }
    }
}
