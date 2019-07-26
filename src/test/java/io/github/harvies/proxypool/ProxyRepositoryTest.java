package io.github.harvies.proxypool;

import io.github.harvies.proxypool.domain.Proxy;
import io.github.harvies.proxypool.repository.ProxyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
public class ProxyRepositoryTest extends BaseTest {
    @Autowired
    private ProxyRepository proxyRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void count() {
        long count = proxyRepository.count(Example.of(Proxy.builder().port(8080).build()));
        log.warn("count:{}", count);
    }

    @Test
    public void deleteAll() {
        proxyRepository.deleteAll();
    }

    @Test
    public void find() {
        List<Proxy> proxyList = mongoTemplate.find(Query.query(Criteria.where("delay")
                .lt(5000))
                .with(Sort.by("stability").descending())
                .limit(100), Proxy.class);
        proxyList.forEach(System.err::println);
    }

    @Test
    public void findById() {
        Proxy proxy = proxyRepository.findById("febd802b62cc340312e550a0201aab79").get();
        log.warn("proxy:{}", proxy);
    }

    @Test
    public void update() {
        Proxy updateResult = mongoTemplate.findAndModify(Query.query(Criteria.where("id").is("febd802b62cc340312e550a0201aab79"))
                , Update.update("gmt_modified", Date.from(Instant.now())), Proxy.class);
        log.warn("updateResult:{}", updateResult);
    }

    /**
     * 查询可用代理数
     */
    @Test
    public void availableNum() {
        long availableCount = mongoTemplate.count(new Query()
                        .addCriteria(Criteria.where("stability").gt(Proxy.STABILITY_DEFAULT))
                        .addCriteria(Criteria.where("delay").lt(Proxy.DELAY_DEFAULT))
                , Proxy.class);
        log.warn("availableCount:{}", availableCount);
    }

}
