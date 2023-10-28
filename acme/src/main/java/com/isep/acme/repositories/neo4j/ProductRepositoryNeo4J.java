package com.isep.acme.repositories.neo4j;

import com.isep.acme.model.Product;
import com.isep.acme.repositories.DataBase;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component("neo4J")
public interface ProductRepositoryNeo4J extends Neo4jRepository<Product, Long>, DataBase {

    default Optional<Product> findBySku(String sku){
        System.out.println("NEO4j");
        return Optional.of(new Product("asd578fgh267", "Pen", "very good nice product"));
    }
}
