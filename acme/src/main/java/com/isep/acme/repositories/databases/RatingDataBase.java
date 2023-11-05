package com.isep.acme.repositories.databases;

import com.isep.acme.model.Rating;
import com.isep.acme.persistance.mongodb.RatingMongo;
import com.isep.acme.persistance.neo4j.RatingNeo4J;

import java.util.Optional;

public interface RatingDataBase {
    Optional<Rating> findByRate(Double rate);
    Rating create(Rating rating);
    RatingNeo4J toRatingNeo4J(Rating rating);
    RatingMongo toRatingMongo(Rating rating);

}
