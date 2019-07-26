package io.github.harvies.proxypool.repository;

import io.github.harvies.proxypool.domain.Proxy;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author harvies
 */
public interface ProxyRepository extends MongoRepository<Proxy, String> {
}
