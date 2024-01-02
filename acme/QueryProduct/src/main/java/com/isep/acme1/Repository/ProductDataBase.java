package com.isep.acme1.Repository;



import com.isep.acme1.Model.Product;
import com.isep.acme1.Model.ProductMongo;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductDataBase {

    Product saveProduct(Product product);

    Product updateProduct(String sku, Product product);

    Optional<Product> findBySku(String sku);

    Optional<Product> findBySkuUser(String sku);

    Iterable<Product> getCatalog();

    ProductMongo toProductMongoUser(Product product);

    void deleteBySku(String sku);

    List<Product> findByDesignation(String designation);

    Product updateBySku(@Param("sku") String sku, Product updatedProduct);

    Optional<Product> findById(Long productID);

    Product create(Product p);

    ProductMongo toProductMongo(Product product);

    ProductMongo toProductMongoCreate(Product product);
}