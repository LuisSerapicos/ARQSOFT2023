package com.isep.acme.repositories;

import com.isep.acme.model.Rating;
import com.isep.acme.persistance.neo4j.RatingNeo4J;
import com.isep.acme.repositories.databases.RatingDataBase;
import org.springframework.stereotype.Repository;
import com.isep.acme.persistance.mongodb.RatingMongo;
import com.isep.acme.repositories.databases.RatingDataBase;

import java.util.Optional;

public class RatingRepository implements RatingDataBase {

    @Override
    public Optional<Rating> findByRate(Double rate) {
        return Optional.empty();
    }

    @Override
    public Rating create(Rating rating) {
        return null;
    }

    @Override
    public RatingMongo toRatingMongo(Rating rating) {
        return null;
    }

    @Override
    public RatingNeo4J toRatingNeo4J(Rating rating) {
        return null;
    }
}
