package io.github.harvies.proxypool.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

/**
 * 各个定时任务使用单独的线程,默认所有定时任务使用一个线程,@Async是每次定时任务执行都使用单独的线程
 *
 * @author harvies
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //设定一个长度10的定时任务线程池
        //todo to modify thread name
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(20));
    }
}