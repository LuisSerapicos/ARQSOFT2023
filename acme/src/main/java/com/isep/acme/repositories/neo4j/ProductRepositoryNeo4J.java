package com.isep.acme.repositories.neo4j;

import com.isep.acme.model.Product;
import com.isep.acme.persistance.neo4j.ProductNeo4J;
import com.isep.acme.repositories.DataBase;
import com.isep.acme.repositories.ProductRepository;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.isep.acme.utils.ProductIterable;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.StreamSupport;


@Component("neo4J")
public class ProductRepositoryNeo4J implements DataBase {

    private final Session session;

    @Autowired
    public ProductRepositoryNeo4J(Session session) {
        this.session = session;
    }

    @Override
    public Optional<Product> findById(Long productID) {
        String cypherQuery = "MATCH (p:ProductNeo4J {id: $productID}) RETURN p";
        Map<String, Object> parameters = Collections.singletonMap("id", productID);
        Result result = session.query(cypherQuery, parameters);

        for (Map<String, Object> row : result) {
            ProductNeo4J product = (ProductNeo4J) row.get("p");
            return Optional.ofNullable((toProduct(product)));
        }

        return Optional.empty();
    }

    @Override
    public Product create(Product p) {
        return null;
    }

    public Optional<Product> findBySku(String sku) {
        String cypherQuery = "MATCH (p:ProductNeo4J {sku: $sku}) RETURN p";
        Map<String, Object> parameters = Collections.singletonMap("sku", sku);
        Result result = session.query(cypherQuery, parameters);

        for (Map<String, Object> row : result) {
            ProductNeo4J product = (ProductNeo4J) row.get("p");
            return Optional.ofNullable((toProduct(product)));
        }

        return Optional.empty();
    }

    public Product saveProduct(Product product) {
        ProductNeo4J save = toProductNeo4J(product);

        Random random = new Random();
        Long randomId = -1 - Math.abs(random.nextLong() % 1000);
        save.setProductID(randomId);

        session.save(save);
        return product;
    }

    @Override
    public List<Product> findByDesignation(String designation) {
        String cypherQuery = "MATCH (p:ProductNeo4J {designation: $designation}) RETURN p";
        Map<String, Object> parameters = Collections.singletonMap("designation", designation);
        Result result = session.query(cypherQuery, parameters);

        for (Map<String, Object> row : result) {
            ProductNeo4J product = (ProductNeo4J) row.get("p");
            return List.of((toProduct(product)));
        }

        return List.of();
    }

    @Override
    public Product updateBySku(String sku, Product updatedProduct) {
        String cypherQuery = "MATCH (p:ProductNeo4J {sku: $sku}) SET p = $updatedProduct RETURN p";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("sku", sku);
        parameters.put("updatedProduct", updatedProduct);
        Result result = session.query(cypherQuery, parameters);

        for (Map<String, Object> row : result) {
            ProductNeo4J product = (ProductNeo4J) row.get("p");
            return toProduct(product);
        }

        return null;
    }

    public Iterable<Product> getCatalog() {
        Collection<ProductNeo4J> result = session.loadAll(ProductNeo4J.class);
        System.out.println("Result: " + result);

        if (result == null) {
            throw(new ResponseStatusException(HttpStatus.NOT_FOUND));
        }

        Iterable<ProductNeo4J> productsNeo4J = new ProductIterable<>(result);
        System.out.println("productsNeo4J: " + productsNeo4J);
        List<Product> product = new ArrayList<>();
        for (ProductNeo4J pd : productsNeo4J) {
            product.add(toProduct(pd));
            System.out.println("Product: " + product);
        }

        return product;
    }
    public void deleteBySku(@Param("sku") String sku) {
        String cypherQuery = "MATCH (p:ProductNeo4J {sku: $sku}) DETACH DELETE p RETURN p";
        Map<String, Object> parameters = Collections.singletonMap("sku", sku);
        session.query(cypherQuery, parameters);
    }

    public Product toProduct(ProductNeo4J productNeo4J) {
        return new Product(productNeo4J.getSku(), productNeo4J.getDesignation(), productNeo4J.getDescription());
    }

    public ProductNeo4J toProductNeo4J(Product product) {
        return new ProductNeo4J(product.getSku(), product.getDesignation(), product.getDescription());
    }
}