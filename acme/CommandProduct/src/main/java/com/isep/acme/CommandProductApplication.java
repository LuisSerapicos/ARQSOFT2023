package com.isep.acme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(
        scanBasePackages = {
                "com.isep.acme"
        }
)
@EnableEurekaClient
public class CommandProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommandProductApplication.class, args);
    }
}