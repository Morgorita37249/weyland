package com.weyland.bishop.service;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Gauge;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final CommandQueueService queueService;
    private final MeterRegistry meterRegistry;

    @PostConstruct
    public void initMetrics() {
        Gauge.builder("weyland.queue.size", queueService, CommandQueueService::getQueueSize)
                .description("Current number of tasks in queue")
                .register(meterRegistry);

        Gauge.builder("weyland.tasks.completed", queueService, CommandQueueService::getCompletedTasksCount)
                .description("Total completed tasks")
                .register(meterRegistry);

        Gauge.builder("weyland.tasks.by_author", queueService,
                        q -> q.getAuthorStats().size())
                .description("Number of unique authors")
                .register(meterRegistry);
    }

    public Map<String, Object> getQueueMetrics() {
        return Map.of(
                "queueSize", queueService.getQueueSize(),
                "completedTasks", queueService.getCompletedTasksCount(),
                "authors", queueService.getAuthorStats()
        );
    }
}
