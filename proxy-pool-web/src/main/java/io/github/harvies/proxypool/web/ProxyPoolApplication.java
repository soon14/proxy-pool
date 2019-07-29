package io.github.harvies.proxypool.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author harvies
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = "io.github.harvies.proxypool")
@EnableMongoRepositories(basePackages = "io.github.harvies.proxypool.core.repository")
public class ProxyPoolApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProxyPoolApplication.class, args);
    }
}
