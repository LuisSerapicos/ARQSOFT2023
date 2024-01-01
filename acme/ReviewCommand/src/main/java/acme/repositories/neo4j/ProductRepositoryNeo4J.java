package acme.repositories.neo4j;

import acme.model.Product;
import acme.persistance.neo4j.ProductNeo4J;
import acme.repositories.databases.ProductDataBase;
import acme.utils.ConvertIterable;
import acme.utils.RandomLongGenerator;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@Component("neo4J")
public class ProductRepositoryNeo4J implements ProductDataBase {

    private final Session session;

    @Autowired
    public ProductRepositoryNeo4J(Session session) {
        this.session = session;
    }

    @Override
    public Optional<Product> findById(Long productID) {
        String cypherQuery = "MATCH (p:ProductNeo4J {id: $productID}) RETURN p";
        Map<String, Object> parameters = Collections.singletonMap("productID", productID);
        Result result = session.query(cypherQuery, parameters);

        for (Map<String, Object> row : result) {
            ProductNeo4J product = (ProductNeo4J) row.get("p");
            return Optional.ofNullable((product.toProduct()));
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
            return Optional.ofNullable((product.toProduct()));
        }

        return Optional.empty();
    }

    public Product saveProduct(Product product) {
        Long randomId = RandomLongGenerator.generateRandomLong();
        product.setProductID(randomId);

        ProductNeo4J save = toProductNeo4J(product);
        save.setProductID(randomId);

        session.save(save);
        return product;
    }

    @Override
    public Product updateProduct(String sku, Product updatedProduct) {
        String cypherQuery = "MATCH (p:ProductNeo4J {sku: $sku}) SET p = $updatedProduct RETURN p";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("sku", sku);
        parameters.put("updatedProduct", updatedProduct);
        Result result = session.query(cypherQuery, parameters);

        for (Map<String, Object> row : result) {
            ProductNeo4J productNeo4J = (ProductNeo4J) row.get("p");
            return productNeo4J.toProduct();
        }

        return null;
    }

    @Override
    public List<Product> findByDesignation(String designation) {
        String cypherQuery = "MATCH (p:ProductNeo4J {designation: $designation}) RETURN p";
        Map<String, Object> parameters = Collections.singletonMap("designation", designation);
        Result result = session.query(cypherQuery, parameters);

        for (Map<String, Object> row : result) {
            ProductNeo4J product = (ProductNeo4J) row.get("p");
            return List.of((product.toProduct()));
        }

        return List.of();
    }

    @Override
    public Product updateBySku(String sku, Product updatedProduct) {
        return null;
    }

    public Iterable<Product> getCatalog() {
        Collection<ProductNeo4J> result = session.loadAll(ProductNeo4J.class);
        System.out.println("Result: " + result);

        if (result == null) {
            throw(new ResponseStatusException(HttpStatus.NOT_FOUND));
        }

        Iterable<ProductNeo4J> productsNeo4J = new ConvertIterable<>(result);
        System.out.println("productsNeo4J: " + productsNeo4J);
        List<Product> product = new ArrayList<>();
        for (ProductNeo4J pd : productsNeo4J) {
            product.add(pd.toProduct());
            System.out.println("Product: " + product);
        }

        return product;
    }
    public void deleteBySku(@Param("sku") String sku) {
        String cypherQuery = "MATCH (p:ProductNeo4J {sku: $sku}) DETACH DELETE p RETURN p";
        Map<String, Object> parameters = Collections.singletonMap("sku", sku);
        session.query(cypherQuery, parameters);
    }

    public ProductNeo4J toProductNeo4J(Product product) {
        return new ProductNeo4J(product.getProductID(), product.getSku(), product.getDesignation(), product.getDescription());
    }
}