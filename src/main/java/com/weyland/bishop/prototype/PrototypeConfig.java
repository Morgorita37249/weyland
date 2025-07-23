package com.weyland.bishop.prototype;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrototypeConfig {
    @Bean
    public String demoBanner() {
        return """
            ************************************************
            * Bishop Prototype успешно запущен!             *
            *                                              *
            * Доступные endpoints:                         *
            * - POST /api/commands                         *
            * - GET  /actuator/metrics                     *
            * - GET  /swagger-ui.html                      *
            ************************************************
            """;
    }
}