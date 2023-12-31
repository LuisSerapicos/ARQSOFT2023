package com.isep.acme1.Service;

import com.isep.acme1.Model.Product;
import com.isep.acme1.Model.ProductDTO;
import com.isep.acme1.Model.ProductDetailDTO;
import com.isep.acme1.Repository.ProductDataBase;
import com.isep.acme1.Sku.SkuGenerator;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.rmi.server.LogStream.log;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final SkuGenerator skuType;

    private final ProductDataBase productDataBase;

    @Autowired
    public ProductServiceImpl(@Value("${sku.interface.generator.default}") String beanName, @Value("${database.interface.default}") String beanName2 , ApplicationContext context) {
        this.productDataBase = context.getBean(beanName2, ProductDataBase.class);
        this.skuType = context.getBean(beanName, SkuGenerator.class);
    }

    @Override
    public Optional<Product> getProductBySku(final String sku) {
        return productDataBase.findBySku(sku);
    }

    @Override
    public Optional<ProductDTO> findBySku(String sku) {
        System.out.println("Service:" +sku);
        final Optional<Product> product = productDataBase.findBySku(sku);
        if (product.isEmpty())
            return Optional.empty();
        else
            return Optional.of(product.get().toDto());
    }


    @Override
    public Iterable<ProductDTO> findByDesignation(final String designation) {
        List<Product> p = productDataBase.findByDesignation(designation);
        List<ProductDTO> pDto = new ArrayList();
        for (Product pd : p) {
            pDto.add(pd.toDto());
        }
        return pDto;
    }

    @Override
    public Iterable<ProductDTO> getCatalog() {
        Iterable<Product> p = productDataBase.getCatalog();
        System.out.println("GET" + p);
        List<ProductDTO> pDto = new ArrayList();
        for (Product pd : p) {
            pDto.add(pd.toDto());
        }
        return pDto;
    }

    public ProductDetailDTO getDetails(String sku) {

        Optional<Product> p = productDataBase.findBySku(sku);
        if (p.isEmpty())
            return null;
        else
            return new ProductDetailDTO(p.get().getSku(), p.get().getDesignation(), p.get().getDescription());
    }

    @Override
    public ProductDTO create(Product manager) {
        return null;
    }

    /*@Override
    public ProductDTO create(final Product product) {
        final Product p = new Product(skuType.generateSku(product.getDesignation()), product.getDesignation(), product.getDescription());
        return productDataBase.saveProduct(p).toDto();
    }*/

    @Override
    public ProductDTO createProduct(final Product product){
        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());
        return productDataBase.saveProduct(p).toDto();
    }

    @Override
    public ProductDTO updateBySku(String sku, Product product) {
        final Optional<Product> productToUpdate = productDataBase.findBySku(sku);

        if (productToUpdate.isEmpty()) return null;

        productToUpdate.get().updateProduct(product);

        System.out.println("Sku update"+ sku);

        Product productUpdated = productDataBase.updateProduct(sku, productToUpdate.get());

        return productUpdated.toDto();
    }

    @Override
    public void deleteBySku(String sku) {
        productDataBase.deleteBySku(sku);
    }

    @Override
    public void createSku() {
        skuType.generateSku("pencil");
    }
}
