package com.weyland.bishop.service;

import com.weyland.bishop.exception.QueueFullException;
import com.weyland.bishop.model.Command;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CommandQueueService {
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2, 5, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100)
    );
    private final AtomicInteger completedTasks = new AtomicInteger();
    private final Map<String, AtomicInteger> authorStats = new ConcurrentHashMap<>();


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
        executor.execute(() -> {
            System.out.println("Executing: " + command.getDescription());
            completedTasks.incrementAndGet();
            authorStats.computeIfAbsent(command.getAuthor(), k -> new AtomicInteger()).incrementAndGet();
        });
    }

    public int getQueueSize() {
        return executor.getQueue().size();
    }
}