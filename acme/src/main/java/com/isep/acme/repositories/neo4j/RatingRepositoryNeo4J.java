package com.isep.acme.repositories.neo4j;

import com.isep.acme.model.Rating;
import com.isep.acme.persistance.mongodb.RatingMongo;
import com.isep.acme.persistance.neo4j.RatingNeo4J;
import com.isep.acme.repositories.databases.RatingDataBase;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component("neo4J2")
public class RatingRepositoryNeo4J implements RatingDataBase {
    private final Session session;

    @Autowired
    public RatingRepositoryNeo4J(Session session) {
        this.session = session;
    }

    @Override
    public Optional<Rating> findByRate(Double rate) {
        String cypherQuery = "MATCH (p:RatingNeo4J {rate: $rate}) RETURN p";
        Map<String, Object> parameters = Collections.singletonMap("rate", rate);
        Result result = session.query(cypherQuery, parameters);

        for (Map<String, Object> row : result) {
            RatingNeo4J ratingNeo4J = (RatingNeo4J) row.get("p");
            return Optional.ofNullable((ratingNeo4J.toRating()));
        }

        return Optional.empty();
    }

    @Override
    public Rating create(Rating rating) {
        RatingNeo4J save = toRatingNeo4J(rating);

        session.save(save);
        return rating;
    }

    public RatingNeo4J toRatingNeo4J(Rating rating) {
        return new RatingNeo4J(rating.getIdRating(), rating.getVersion(), rating.getRate());
    }

    @Override
    public RatingMongo toRatingMongo(Rating rating) {
        return null;
    }
}
