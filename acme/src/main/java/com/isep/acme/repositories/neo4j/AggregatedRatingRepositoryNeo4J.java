package com.isep.acme.repositories.neo4j;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;
import com.isep.acme.persistance.neo4j.AggregatedRatingNeo4J;
import com.isep.acme.persistance.neo4j.ProductNeo4J;
import com.isep.acme.repositories.databases.AggregatedRatingDataBase;
import com.isep.acme.repositories.databases.ProductDataBase;
import com.isep.acme.utils.RandomLongGenerator;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component("neo4J4")
public class AggregatedRatingRepositoryNeo4J implements AggregatedRatingDataBase {

    private final Session session;

    private final ProductDataBase productDataBase;

    public AggregatedRatingRepositoryNeo4J(@Value("${database.interface.default}") String beanName, ApplicationContext context, Session session) {
        this.productDataBase = context.getBean(beanName, ProductDataBase.class);
        this.session = session;
    }

    @Override
    public Optional<AggregatedRating> findByProductId(Product product) {
        String cypherQuery = "MATCH (p:ProductNeo4J {productId: $productId})<-[:PRODUCT]-(a:AggregatedRatingNeo4J) " +
                "OPTIONAL MATCH (a)-[:PRODUCT]->(product:ProductNeo4J)\n" +
                "RETURN a, product";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("productId", product.getProductID());
        Result result = session.query(cypherQuery, parameters);

        for (Map<String, Object> row : result) {
            AggregatedRatingNeo4J aggregatedRatingNeo4J = (AggregatedRatingNeo4J) row.get("a");
            ProductNeo4J productNeo4J = (ProductNeo4J) row.get("product");
            aggregatedRatingNeo4J.setProduct(productNeo4J);

            return Optional.ofNullable(aggregatedRatingNeo4J.toAggregatedRating());
        }

        return Optional.empty();
    }

    @Override
    public AggregatedRating create(AggregatedRating rating) {
        Long randomId = RandomLongGenerator.generateRandomLong();
        rating.setAggregatedId(randomId);

        AggregatedRatingNeo4J save = toAggregatedRatingNeo4J(rating);
        save.setAggregatedId(randomId);

        session.save(save);
        return rating;
    }

    public AggregatedRatingNeo4J toAggregatedRatingNeo4J(AggregatedRating aggregatedRating) {
        return new AggregatedRatingNeo4J(aggregatedRating.getAggregatedId(), aggregatedRating.getAverage(), productDataBase.toProductNeo4J(aggregatedRating.getProduct()));
    }
}
