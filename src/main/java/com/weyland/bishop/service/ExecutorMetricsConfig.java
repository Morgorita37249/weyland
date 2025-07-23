package com.weyland.bishop.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ExecutorMetricsConfig {

    @Bean
    public Gauge queueSizeGauge(MeterRegistry registry, CommandQueueService queueService) {
        return Gauge.builder("bishop.queue.size", queueService,
                        qs -> qs.getCurrentQueueSize())
                .description("Current queue size")
                .register(registry);
    }

    @Bean
    public Gauge activeThreadsGauge(MeterRegistry registry, CommandQueueService queueService) {
        return Gauge.builder("bishop.active.threads", queueService,
                        qs -> qs.getActiveThreadCount())
                .description("Active processing threads")
                .register(registry);
    }

    @Bean
    public MeterBinder authorMetricsBinder(CommandQueueService queueService) {
        return registry -> {
            queueService.getAuthorStatistics().forEach((author, count) -> {
                Gauge.builder("bishop.author.commands", count, Integer::intValue)
                        .tag("author", author)
                        .register(registry);
            });
        };
    }
}