package io.github.harvies.proxypool.pipeline;

import com.alibaba.fastjson.JSON;
import io.github.harvies.proxypool.domain.Proxy;
import io.github.harvies.proxypool.service.ProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * @author harvies
 */
@Slf4j
@Service
public class ProxyMongoPipeline implements Pipeline {

    @Autowired
    private ProxyService proxyService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map<String, Object> resultItemsAll = resultItems.getAll();
        resultItemsAll.forEach((s, o) -> {
            Proxy proxy = JSON.parseObject(JSON.toJSONString(o), Proxy.class);
            try {
//                proxy.generateId();
                proxyService.save(proxy);
                log.warn("proxy origin:{} ip:{} port:{} save success!", proxy.getOrigin(), proxy.getIp(), proxy.getPort());
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("save Exception proxy:{}", proxy, e);
                }
            }
        });
    }
}
