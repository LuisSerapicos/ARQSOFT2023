package com.isep.acme.Repository;



import com.isep.acme.ConvertIterable;
import com.isep.acme.Model.Product;
import com.isep.acme.Model.ProductMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("mongoDB")
public class ProductRepositoryMongoDB implements ProductDataBase {
    private final MongoTemplate mongoTemplate;


    @Autowired
    public ProductRepositoryMongoDB(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Allow to save a Product in Database
     *
     * @param product
     * @return Product
     */
    @Override
    public Product saveProduct(Product product) {
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        ProductMongo save = toProductMongo(product);
        if (findBySku(save.sku).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        mongoTemplate.save(save);
        return product;
    }

    @Override
    public Product updateProduct(String sku, Product product) {
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        ProductMongo save = toProductMongo(product);
        if (findBySku(save.sku).isPresent()) {
            Query query = new Query(Criteria.where("sku").is(save.sku));
            Update update = new Update();
            update.set("designation", product.getDesignation());
            update.set("description", product.getDescription());

            mongoTemplate.updateFirst(query, update, ProductMongo.class);
            return product;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Optional<Product> findBySku(String sku) {
        Query query = new Query(Criteria.where("sku").is(sku));
        ProductMongo product = mongoTemplate.findOne(query, ProductMongo.class);
        if (product == null) {
            System.out.println("Product don't exist");
            return Optional.empty();
        }
        return Optional.ofNullable(product.toProduct());
    }

    ;

    public Iterable<Product> getCatalog() {
        Query query = new Query();
        List<ProductMongo> productMongo = mongoTemplate.find(query, ProductMongo.class);
        if (productMongo.isEmpty()) {
            System.out.println("Product don't exist");
            return null;
        }
        Iterable<ProductMongo> productIterableMongo = new ConvertIterable<>(productMongo);
        List<Product> product = new ArrayList<>();
        for (ProductMongo pd : productIterableMongo) {
            product.add(pd.toProduct());
        }
        return product;
    }

    public Optional<Product> findById(Long productID) {
        return null;
    }

    @Override
    public Product create(Product p) {
        return null;
    }

    @Override
    public ProductMongo toProductMongo(Product product) {
        return new ProductMongo(product.getProductID(), product.sku, product.getDesignation(), product.getDescription());
    }

    @Override
    public void deleteBySku(String sku) {
        System.out.println(sku);
        Query query = new Query(Criteria.where("sku").is(sku));
        System.out.println(query);
        mongoTemplate.remove(query, ProductMongo.class);
    }

    @Override
    public List<Product> findByDesignation(String designation) {
        Query query = new Query(Criteria.where("designation").is(designation));
        List<ProductMongo> productMongo = mongoTemplate.find(query, ProductMongo.class);
        if (productMongo.isEmpty()) {
            System.out.println("Product don't exist");
            return null;
        }
        List<Product> product = new ArrayList<>();
        for (ProductMongo pd : productMongo) {
            product.add(pd.toProduct());
        }
        return product;
    }

    @Override
    public Product updateBySku(String sku, Product updatedProduct) {
        return null;
    }

}
