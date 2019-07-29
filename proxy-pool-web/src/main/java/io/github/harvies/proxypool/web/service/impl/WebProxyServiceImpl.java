package io.github.harvies.proxypool.web.service.impl;

import com.mongodb.client.result.UpdateResult;
import io.github.harvies.proxypool.api.domain.Proxy;
import io.github.harvies.proxypool.core.repository.ProxyRepository;
import io.github.harvies.proxypool.web.query.ProxyQuery;
import io.github.harvies.proxypool.web.service.WebProxyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author harvies
 */
@Slf4j
@Service
public class WebProxyServiceImpl implements WebProxyService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ProxyRepository proxyRepository;

    @Override
    public long count(ProxyQuery proxyQuery) {
        Query query = where(proxyQuery);
        return mongoTemplate.count(query
                , Proxy.class);
    }

    private Query where(ProxyQuery proxyQuery) {
        Query query = new Query();
        /**
         * 稳定性
         */
        if (proxyQuery.getStabilityGt() != null) {
            query.addCriteria(Criteria.where("stability").gt(proxyQuery.getStabilityGt()));
        } else {
            query.addCriteria(Criteria.where("stability").gt(Proxy.STABILITY_DEFAULT));
        }

        /**
         * 延迟
         */
        if (proxyQuery.getDelayLte() != null) {
            query.addCriteria(Criteria.where("delay").lte(proxyQuery.getDelayLte()));
        } else {
            query.addCriteria(Criteria.where("delay").lte(Proxy.DELAY_DEFAULT));
        }

        /**
         * 协议
         */
        if (proxyQuery.getProtocol() != null) {
            query.addCriteria(Criteria.where("protocol").is(proxyQuery.getProtocol()));
        }

        /**
         * 国家代码
         */
        if (proxyQuery.getCountryCode() != null) {
            query.addCriteria(Criteria.where("country_code").is(proxyQuery.getCountryCode()));
        }
        /**
         * 来源
         */
        if (proxyQuery.getOrigin() != null) {
            query.addCriteria(Criteria.where("origin").is(proxyQuery.getOrigin()));
        }
        /**
         * 能否上google
         */
        if (proxyQuery.getGoogle() != null) {
            query.addCriteria(Criteria.where("google").is(proxyQuery.getGoogle()));
        }
        /**
         * ip
         */
        if (proxyQuery.getIp() != null) {
            query.addCriteria(Criteria.where("ip").is(proxyQuery.getIp()));
        }
        /**
         * 端口
         */
        if (proxyQuery.getPort() != null) {
            query.addCriteria(Criteria.where("port").is(proxyQuery.getPort()));
        }
        /**
         * 按高存活高可用低延迟排序
         */
        query.with(Sort.by(Sort.Order.desc("survival_days"), Sort.Order.desc("stability"), Sort.Order.asc("delay")));
        return query;
    }

    @Override
    public List<Proxy> list(ProxyQuery proxyQuery) {
        Query query = where(proxyQuery);
        query.skip(proxyQuery.getOffset() * proxyQuery.getLimit()).limit(proxyQuery.getLimit());
        return mongoTemplate.find(query
                , Proxy.class);
    }

    @Override
    public List<Proxy> findWillBeCheckedProxyList() {
        Query query = new Query();
        query.addCriteria(Criteria.where("stability").gte(0))
                .with(Sort.by(Sort.Order.asc("gmtModified")))
                .limit(1000);
        return mongoTemplate.find(query, Proxy.class);
    }

    @Override
    public Proxy get(ProxyQuery proxyQuery) {
        Query query = where(proxyQuery);
        long count = mongoTemplate.count(query, Proxy.class);
        if (count == 0) {
            return null;
        }
        long nextLong = RandomUtils.nextLong(0, count);
        query.skip(nextLong).limit(1);
        return mongoTemplate.findOne(query, Proxy.class);
    }

    @Override
    public Boolean deleteById(String id) {
        proxyRepository.deleteById(id);
        return true;
    }


    @Override
    public long decrementStability(Proxy proxy) {
        UpdateResult updateResult = incrStability(proxy, -1);
        return updateResult.getModifiedCount();
    }

    @Override
    public long incrementStability(Proxy proxy) {
        UpdateResult updateResult = incrStability(proxy, 1);
        return updateResult.getModifiedCount();
    }

    private UpdateResult incrStability(Proxy proxy, Number number) {
        Query query = new Query();

        if (proxy.getId() != null) {
            query.addCriteria(Criteria.where("id").is(proxy.getId()));
        }
        if (proxy.getIp() != null && proxy.getPort() != null) {
            query.addCriteria(Criteria.where("ip").is(proxy.getIp()));
            query.addCriteria(Criteria.where("port").is(proxy.getPort()));
        }

        Update update = new Update();
        update.inc("stability", number);
        return mongoTemplate.updateFirst(query, update, Proxy.class);
    }

}
