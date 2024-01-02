package com.isep.acme1.Service;





import com.isep.acme1.Model.Product;
import com.isep.acme1.Model.ProductDTO;
import com.isep.acme1.Model.ProductDetailDTO;
import com.isep.acme1.Model.ProductUserDTO;

import java.util.Optional;


public interface ProductService {

    Optional<ProductDTO> findBySku(final String sku);

    Optional<Product> getProductBySku(final String sku );

    Iterable<ProductDTO> findByDesignation(final String designation);

    Iterable<ProductUserDTO> getCatalog();

    ProductDetailDTO getDetails(final String sku);

    ProductDTO create(final Product manager);

    ProductDTO createProduct(final Product product);

    ProductDTO updateBySku(final String sku, final Product product);

    void deleteBySku(final String sku);

    void createSku();
}
