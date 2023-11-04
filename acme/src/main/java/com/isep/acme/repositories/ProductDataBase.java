package com.isep.acme.repositories;

import com.isep.acme.model.Product;
import org.springframework.data.repository.query.Param;
import java.util.List;

import java.util.Optional;

public interface ProductDataBase {

    Product saveProduct(Product product);

    Product updateProduct(Product product);

    Optional<Product> findBySku(String sku);
    Iterable<Product> getCatalog();
    void deleteBySku(String sku);

    List<Product> findByDesignation(String designation);

    Product updateBySku(@Param("sku") String sku, Product updatedProduct);

    Optional<Product> findById(Long productID);

    Product create(Product p);
}