package io.github.harvies.proxypool.factory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.harvies.proxypool.domain.ProxyOrigin;
import io.github.harvies.proxypool.processor.AbstractProcessor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author harvies
 */
public class ProcessorFactory {

    private static List<AbstractProcessor> abstractProcessorList = Lists.newArrayList();
    private static Set<Integer> proxyOriginEnumSet = Sets.newHashSet();

    public static void register(AbstractProcessor abstractProcessor) {
        ProxyOrigin origin = abstractProcessor.getOrigin();
        if (origin == null) {
            throw new RuntimeException("代理来源未配置!");
        }
        if (proxyOriginEnumSet.contains(origin.getId())) {
            throw new RuntimeException("代理来源配置重复!");
        }
        proxyOriginEnumSet.add(origin.getId());

        abstractProcessorList.add(abstractProcessor);
    }
}
