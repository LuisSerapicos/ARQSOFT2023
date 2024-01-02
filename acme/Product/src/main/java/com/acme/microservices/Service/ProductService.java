package com.acme.microservices.Service;

import com.acme.microservices.Model.Product;
import com.acme.microservices.Model.ProductDTO;
import com.acme.microservices.Model.ProductDetailDTO;

import java.util.Optional;


public interface ProductService {

    Optional<ProductDTO> findBySku(final String sku);

    Optional<Product> getProductBySku(final String sku );

    Iterable<ProductDTO> findByDesignation(final String designation);

    Iterable<ProductDTO> getCatalog();

    ProductDetailDTO getDetails(final String sku);

    ProductDTO create(final Product manager);

    ProductDTO updateBySku(final String sku, final Product product);

    void deleteBySku(final String sku);

    void createSku();
}
