package io.github.harvies.proxypool.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.mongodb.client.result.UpdateResult;
import io.github.harvies.proxypool.domain.Proxy;
import io.github.harvies.proxypool.domain.ProxyResult;
import io.github.harvies.proxypool.enums.ProxyAnonymousLevelEnum;
import io.github.harvies.proxypool.service.CheckProxyService;
import lombok.extern.slf4j.Slf4j;
import net.dongliu.requests.Proxies;
import net.dongliu.requests.Requests;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

/**
 * @author harvies
 */
@Service
@Slf4j
public class CheckProxyServiceImpl implements CheckProxyService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ProxyResult check(Proxy proxy) {
        if (log.isDebugEnabled()) {
            log.debug("check proxy ip:{} port:{} begin!", proxy.getIp(), proxy.getPort());
        }
        ProxyResult result = getResult(proxy);
        updateProxyInfo(proxy, result);
        if (result != null) {
            if (log.isDebugEnabled()) {
                log.debug("check proxy ip:{} port:{} end! result:{}", proxy.getIp(), proxy.getPort(), result.getStatus());
            }
        }
        return result;
    }

    private void updateProxyInfo(Proxy proxy, ProxyResult proxyResult) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(proxy.getId()));
        UpdateResult updateResult;
        if (proxyResult == null) {
            /**
             * 延迟还原,稳定性减1
             */
            Integer checkContinuousFailedNum = proxy.getCheckContinuousFailedNum();
            updateResult = mongoTemplate.updateFirst(query
                    , Update.update("gmt_modified", Date.from(Instant.now()))
                            .set("delay", Proxy.DELAY_DEFAULT)
                            .inc("check_continuous_failed_num", 1)
                            .inc("stability", -1 * Math.pow(2, checkContinuousFailedNum == null ? 0 : checkContinuousFailedNum))
                    , Proxy.class);
        } else {
            Update update = Update.update("gmt_modified", Date.from(Instant.now()));
            updateResult = mongoTemplate.updateFirst(query
                    , update
                            .set("delay", proxyResult.getDelay())
                            .set("lat", proxyResult.getLat())
                            .set("lon", proxyResult.getLon())
                            .set("country", proxyResult.getCountry())
                            .set("region_name", proxyResult.getRegionName())
                            .set("isp", proxyResult.getIsp())
                            .set("city", proxyResult.getCity())
                            .inc("stability", 1)
                            .set("country_code", proxyResult.getCountryCode())
                            .set("google", proxyResult.isGoogle())
                            //连续失败数归零
                            .set("check_continuous_failed_num", 0)
                            //匿名检测
                            .set("anonymous_level",
                                    Objects.equals(proxyResult.getQuery(), proxy.getIp())
                                            ? ProxyAnonymousLevelEnum.HIGH.getValue() : ProxyAnonymousLevelEnum.LOW.getValue())
                    , Proxy.class);
        }

        if (log.isDebugEnabled()) {
            log.debug("updateResult:{}", updateResult);
        }
    }

    private ProxyResult getResult(Proxy proxy) {

        long currentTimeMillis = System.currentTimeMillis();
        try {
            java.net.Proxy proxyJ;
            if (Objects.equals(proxy.getType(), java.net.Proxy.Type.HTTP)) {
                proxyJ = Proxies.httpProxy(proxy.getIp(), proxy.getPort());
            } else {
                proxyJ = Proxies.socksProxy(proxy.getIp(), proxy.getPort());
            }
            String text = Requests.get("http://ip-api.com/json/").proxy(proxyJ).send().readToText();
            ProxyResult proxyResult = JSON.parseObject(text, ProxyResult.class);
            if (proxyResult == null || StringUtils.isBlank(proxyResult.getQuery())) {
                return null;
            }
            /**
             *只需要检测一次就行了
             */
            if (!proxy.isGoogle()) {
                proxyResult.setGoogle(google(proxyJ));
            } else {
                proxyResult.setGoogle(true);
            }

            long delay = System.currentTimeMillis() - currentTimeMillis;
            proxyResult.setDelay(delay);
            return proxyResult;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("getResult Exception", e);
            }
        }
        return null;
    }

    /**
     * 能否上Google
     *
     * @param proxyJ
     * @return
     */
    private boolean google(java.net.Proxy proxyJ) {
        try {
            String readToText = Requests.get("https://www.google.com").proxy(proxyJ).send().readToText();
            if (!Strings.isNullOrEmpty(readToText)) {
                return true;
            }
        } catch (Exception e) {
            log.debug("check google Exception proxyJ:{}", proxyJ, e);
        }
        return false;
    }
}
