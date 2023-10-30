package com.isep.acme.repositories.neo4j;

import com.isep.acme.model.Product;
import com.isep.acme.persistance.neo4j.ProductNeo4J;
import com.isep.acme.repositories.DataBase;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;


@Component("neo4J")
public class ProductRepositoryNeo4J extends Neo4jRepository<ProductNeo4J, Long>, DataBase {

    Optional<Product> findBySku(String sku);
    @Query("MATCH (p:Product) RETURN p")
    Optional<Product> getCatalog();

    @Transactional
    @Modifying
    @Query("MATCH (p:Product {sku: $sku}) DETACH DELETE p")
    void deleteBySku(@Param("sku") String sku);
}