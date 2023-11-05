package com.isep.acme.repositories.mongodb;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;
import com.isep.acme.persistance.mongodb.AggregatedRatingMongo;
import com.isep.acme.repositories.databases.AggregatedRatingDataBase;
import com.isep.acme.repositories.databases.ProductDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component("mongoDB4")
public class AggregatedRatingRepositoryMongoDB implements AggregatedRatingDataBase {

    private final MongoTemplate mongoTemplate;

    private final ProductDataBase productDataBase;

    @Autowired
    public AggregatedRatingRepositoryMongoDB(MongoTemplate mongoTemplate, @Value("${database.interface.default}") String beanName, ApplicationContext context) {
        this.mongoTemplate = mongoTemplate;
        this.productDataBase = context.getBean(beanName, ProductDataBase.class);
    }

    @Override
    public Optional<AggregatedRating> findByProductId(Product product) {
        Query query = new Query(Criteria.where("product.productID").is(product.getProductID()));
        AggregatedRatingMongo reviewMongo = mongoTemplate.findOne(query, AggregatedRatingMongo.class);
        if(reviewMongo == null) return Optional.empty();
        return Optional.of(reviewMongo.toAggregatedRating());
    }

    @Override
    public AggregatedRating create(AggregatedRating rating) {
        if (rating == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        AggregatedRatingMongo save = toAggregatedMongo(rating);
        mongoTemplate.save(save);
        return save.toAggregatedRating();
    }

    private AggregatedRatingMongo toAggregatedMongo(AggregatedRating aggregatedRating) {
        return new AggregatedRatingMongo(aggregatedRating.getAverage(), productDataBase.toProductMongo(aggregatedRating.getProduct()));
    }


}
