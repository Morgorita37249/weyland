package com.weyland.bishop.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorMetricsConfig {

    @Bean
    public Gauge queueSizeGauge(MeterRegistry registry, CommandQueueService service) {
        return Gauge.builder("bishop.queue.size",
                        service, s -> s.getQueueSize())
                .register(registry);
    }
}