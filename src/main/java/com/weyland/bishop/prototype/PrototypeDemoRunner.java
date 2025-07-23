package com.weyland.bishop.prototype;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PrototypeDemoRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("\n=== Bishop Prototype Demo ===");
        System.out.println("1. Отправьте CRITICAL-команду:");
        System.out.println("   POST /api/commands {priority: CRITICAL}");
        System.out.println("2. Проверьте метрики:");
        System.out.println("   GET /actuator/metrics/bishop.queue.size");
    }
}