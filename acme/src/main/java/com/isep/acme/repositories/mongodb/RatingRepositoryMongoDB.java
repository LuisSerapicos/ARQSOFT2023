package com.isep.acme.repositories.mongodb;

import com.isep.acme.model.Rating;
import com.isep.acme.persistance.mongodb.RatingMongo;
import com.isep.acme.repositories.databases.RatingDataBase;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component("mongoDB2")
public class RatingRepositoryMongoDB implements RatingDataBase {

    private final MongoTemplate mongoTemplate;

    public RatingRepositoryMongoDB(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<Rating> findByRate(Double rate) {
        Query query = new Query(Criteria.where("rate").is(rate));
        RatingMongo ratingMongo = mongoTemplate.findOne(query, RatingMongo.class);
        if (ratingMongo == null) {
            System.out.println("Rating don't exist");
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return Optional.ofNullable(ratingMongo.toRating());
    }

    @Override
    public Rating create(Rating rating) {
        RatingMongo save = toRatingMongo(rating);
        if (findByRate(save.getRate()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        mongoTemplate.save(save);
        return rating;
    }

    @Override
    public RatingMongo toRatingMongo(Rating rating) {
        return new RatingMongo(rating.getIdRating(), rating.getVersion(), rating.getRate());
    }
}
