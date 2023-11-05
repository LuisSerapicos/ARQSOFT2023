package com.isep.acme.repositories;

import com.isep.acme.persistance.neo4j.ProductNeo4J;
import com.isep.acme.repositories.databases.ProductDataBase;
import org.springframework.stereotype.Component;

import com.isep.acme.model.Product;
import com.isep.acme.persistance.mongodb.ProductMongo;
import com.isep.acme.repositories.databases.ProductDataBase;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component("sql")
public class ProductRepository implements ProductDataBase {

    @Override
    public Product updateBySku(String sku, Product updatedProduct) {
        return null;
    }

    @Override
    public Optional<Product> findById(Long productID) {
        return Optional.empty();
    }

    @Override
    public Product create(Product p) {
        return null;
    }

    @Override
    public ProductNeo4J toProductNeo4J(Product product) {
        return null;
    }
    @Override
    public ProductMongo toProductMongo(Product product) {
        return null;
    }

    @Override
    public Product saveProduct(Product product) {
        return null;
    }

    @Override
    public Product updateProduct(String sku, Product product) {
        return null;
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        System.out.println("Repository SQL: " + sku);
        return Optional.empty();
    }

    @Override
    public Iterable<Product> getCatalog() {
        return null;
    }

    @Override
    public void deleteBySku(String sku) {
    }

    @Override
    public List<Product> findByDesignation(String designation) {
        return null;
    }

    //Obtain the catalog of products -> Catalog: show sku and designation of all products


    //Obtain the details of products -> Destails: show sku, designation and description of all products

    //Delete the product when given the SKU


  /*  @Query("SELECT p FROM ProdImage p WHERE p.id=:id")
    Optional<Product> findById(Long  id); */

}

