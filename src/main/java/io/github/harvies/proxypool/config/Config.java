package io.github.harvies.proxypool.config;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author harvies
 */
@Configuration
@Data
@ConfigurationProperties(value = "proxy-pool")
public class Config {
    /**
     * 启用的代理ids(英文逗号分割)
     */
    private String enableProcessors;

    /**
     * 开启检测
     */
    private boolean enableCheck = true;


    public List<Integer> getEnableProcessorIdList() {
        if (Strings.isNullOrEmpty(enableProcessors)) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(Splitter.on(',').split(enableProcessors))
                .stream().map(Integer::valueOf).collect(Collectors.toList());
    }


}
