package com.weyland.bishop.prototype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "com.weyland.bishop")
@Import(PrototypeConfig.class)
public class BishopPrototypeApp {
    public static void main(String[] args) {
        SpringApplication.run(BishopPrototypeApp.class, args);
    }
}