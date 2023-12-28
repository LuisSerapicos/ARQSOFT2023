package com.isep.acme1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class QueryProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(QueryProductApplication.class, args);
    }
}