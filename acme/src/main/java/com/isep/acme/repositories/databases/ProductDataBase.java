package com.isep.acme.repositories.databases;

import com.isep.acme.model.Product;
import com.isep.acme.persistance.neo4j.ProductNeo4J;
import com.isep.acme.persistance.mongodb.ProductMongo;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductDataBase {

    Product saveProduct(Product product);

    Product updateProduct(String sku, Product product);

    Optional<Product> findBySku(String sku);
    Iterable<Product> getCatalog();
    void deleteBySku(String sku);

    List<Product> findByDesignation(String designation);

    Product updateBySku(@Param("sku") String sku, Product updatedProduct);

    Optional<Product> findById(Long productID);

    Product create(Product p);

    ProductMongo toProductMongo(Product product);

    ProductNeo4J toProductNeo4J(Product product);
}