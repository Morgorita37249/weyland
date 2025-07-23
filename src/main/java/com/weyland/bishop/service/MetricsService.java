package com.weyland.bishop.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final MeterRegistry meterRegistry;

    // Счетчики команд
    private Counter criticalCommandsCounter;
    private Counter commonCommandsCounter;

    @PostConstruct
    public void init() {
        criticalCommandsCounter = Counter.builder("bishop.commands.critical")
                .description("Total CRITICAL commands")
                .register(meterRegistry);

        commonCommandsCounter = Counter.builder("bishop.commands.common")
                .description("Total COMMON commands")
                .register(meterRegistry);
    }

    public void incrementCritical() {
        criticalCommandsCounter.increment();
    }

    public void incrementCommon() {
        commonCommandsCounter.increment();
    }
}
