package acme.config;

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
                .database("ReviewCommand")
                .credentials("neo4j", "password") // Neo4j credentials
                .build(), "acme.persistance.neo4j.*");
    }

    @Bean
    public Session session() {
        return sessionFactory().openSession();
    }
}
