package com.isep.acme.config;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Neo4JConfig {
    @Bean
    public SessionFactory sessionFactory() {
        // Configure your session factory here
        return new SessionFactory(new org.neo4j.ogm.config.Configuration.Builder()
                .uri("neo4j://localhost:7687") // Neo4j URI
                .credentials("neo4j", "12345678") // Neo4j credentials
                .database("ACME")
                .build(), "com.isep.acme.persistance.neo4j.ProductNeo4J");
    }

    @Bean
    public Session session() {
        return sessionFactory().openSession();
    }
}
