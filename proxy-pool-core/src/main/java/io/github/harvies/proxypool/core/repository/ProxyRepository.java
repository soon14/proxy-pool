package io.github.harvies.proxypool.core.repository;

import io.github.harvies.proxypool.api.domain.Proxy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author harvies
 */
@Repository
public interface ProxyRepository extends MongoRepository<Proxy, String> {
}
