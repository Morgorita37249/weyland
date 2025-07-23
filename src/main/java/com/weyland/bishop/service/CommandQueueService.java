package com.weyland.bishop.service;

import com.weyland.bishop.audit.AuditMode;
import com.weyland.bishop.audit.WeylandWatchingYou;
import com.weyland.bishop.exception.QueueFullException;
import com.weyland.bishop.model.Command;
import com.weyland.bishop.model.Priority;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@RequiredArgsConstructor
public class CommandQueueService {
    private final MetricsService metricsService;
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,
            5,
            60, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100)
    );

    private final AtomicInteger completedTasks = new AtomicInteger();
    private final Map<String, AtomicInteger> authorStats = new ConcurrentHashMap<>();
    private final KafkaTemplate<String, String> kafkaTemplate; // Для fallback-аудита

    public int getCompletedTasksCount() {
        return completedTasks.get();
    }

    public Map<String, Integer> getAuthorStats() {
        Map<String, Integer> stats = new HashMap<>();
        authorStats.forEach((author, count) -> stats.put(author, count.get()));
        return stats;
    }

    public void addCommand(Command command) throws QueueFullException {
        if (executor.getQueue().remainingCapacity() == 0) {
            throw new QueueFullException("Command queue is full");
        }

        if (command.getPriority() == Priority.CRITICAL) {
            executeCriticalCommand(command);
        } else {
            executeCommonCommand(command);
        }
    }

    public int getQueueSize() {
        return executor.getQueue().size();
    }


    public void executeCriticalCommand(Command command) {
        processCommand(command, 0);
    }

    private void executeCommonCommand(Command command) {
        // COMMON команды добавляются в очередь с задержкой
        executor.execute(() -> {
            try {
                Thread.sleep(2000); // Имитация обработки
                processCommand(command, 2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    private void processCommand(Command command, long delayMs) {
        if (command.getPriority() == Priority.CRITICAL) {
            metricsService.incrementCritical();
        } else {
            metricsService.incrementCommon();
        }
        String result = executeAndAuditCommand(command, delayMs);
        System.out.println("[DEBUG] Command processed: " + result);
    }
    public Map<String, Integer> getAuthorStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        authorStats.forEach((author, count) -> stats.put(author, count.get()));
        return stats;
    }

    @WeylandWatchingYou(mode = AuditMode.KAFKA)
    private String executeAndAuditCommand(Command command, long delayMs) {

        completedTasks.incrementAndGet();
        authorStats.computeIfAbsent(command.getAuthor(), k -> new AtomicInteger()).incrementAndGet();

        return String.format(
                "Command executed: %s | Priority: %s | Author: %s | Delay: %dms",
                command.getDescription(),
                command.getPriority(),
                command.getAuthor(),
                delayMs
        );

    }
    public int getCurrentQueueSize() {
        return executor.getQueue().size();
    }

    public int getActiveThreadCount() {
        return executor.getActiveCount();
    }

}