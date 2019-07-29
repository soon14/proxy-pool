package io.github.harvies.proxypool.core.service.impl;

import com.mongodb.client.result.DeleteResult;
import io.github.harvies.proxypool.core.repository.ProxyRepository;
import io.github.harvies.proxypool.core.service.ProxyService;
import io.github.harvies.proxypool.api.domain.Proxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author harvies
 */
@Slf4j
@Service
public class ProxyServiceImpl implements ProxyService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ProxyRepository proxyRepository;


    @Override
    public List<Proxy> findWillBeCheckedProxyList() {
        Query query = new Query();
        query.addCriteria(Criteria.where("stability").gte(0))
                .with(Sort.by(Sort.Order.asc("gmtModified")))
                .limit(1000);
        return mongoTemplate.find(query, Proxy.class);
    }

    @Override
    public Proxy save(Proxy proxy) {
        if (proxy.getGmtCreate() == null) {
            proxy.setGmtCreate(new Date());
        }
        if (proxy.getGmtModified() == null) {
            proxy.setGmtModified(new Date());
        }
        if (proxy.getCheckContinuousFailedNum() == null) {
            proxy.setCheckContinuousFailedNum(0);
        }
        if (proxy.getDelay() == null) {
            proxy.setDelay(Proxy.DELAY_DEFAULT);
        }
        if (proxy.getStability() == null) {
            proxy.setStability(Proxy.STABILITY_DEFAULT);
        }
        return proxyRepository.save(proxy);
    }

    @Override
    public long clean() {
        Query query = new Query();
        query.addCriteria(Criteria.where("stability").lt(0));
        DeleteResult deleteResult = mongoTemplate.remove(query, Proxy.class);
        return deleteResult.getDeletedCount();
    }
}
