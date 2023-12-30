package com.isep.acme.Service;



import com.isep.acme.Model.Product;
import com.isep.acme.Model.ProductDTO;
import com.isep.acme.Model.ProductDetailDTO;

import java.util.Optional;


public interface ProductService {

    Optional<ProductDTO> findBySku(final String sku);

    Optional<Product> getProductBySku(final String sku );

    Iterable<ProductDTO> findByDesignation(final String designation);

    Iterable<ProductDTO> getCatalog();

    ProductDetailDTO getDetails(final String sku);

    ProductDTO create(final Product manager);

    ProductDTO updateBySku(final String sku, final Product product);

    Boolean approveByUser(final String sku, final String username);

    void deleteBySku(final String sku);

    void createSku();

    Boolean verifyIfExists(String username);
}
