package com.isep.acme.bootstrapper;

import com.isep.acme.model.Rating;
import com.isep.acme.repositories.neo4j.RatingRepositoryNeo4J;
import com.isep.acme.utils.RandomLongGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.stereotype.Component;

@Component
@EnableNeo4jRepositories("com.isep.acme.repositories.neo4j")
public class RatingBootstrapperNeo4J implements CommandLineRunner {
    @Autowired
    private RatingRepositoryNeo4J ratingRepositoryNeo4J;

    @Override
    public void run(String... args) throws Exception {
        if(ratingRepositoryNeo4J.findByRate(0.5).isEmpty()) {
            Rating rate05 = new Rating(RandomLongGenerator.generateRandomLong(), 1,0.5);
            ratingRepositoryNeo4J.create(rate05);
        }

        if(ratingRepositoryNeo4J.findByRate(1.0).isEmpty()) {
            Rating rate1 = new Rating(RandomLongGenerator.generateRandomLong(), 1,1.0);
            ratingRepositoryNeo4J.create(rate1);
        }

        if(ratingRepositoryNeo4J.findByRate(1.5).isEmpty()) {
            Rating rate15 = new Rating(RandomLongGenerator.generateRandomLong(), 1,1.5);
            ratingRepositoryNeo4J.create(rate15);
        }

        if(ratingRepositoryNeo4J.findByRate(2.0).isEmpty()) {
            Rating rate2 = new Rating(RandomLongGenerator.generateRandomLong(), 1,2.0);
            ratingRepositoryNeo4J.create(rate2);
        }

        if(ratingRepositoryNeo4J.findByRate(2.5).isEmpty()) {
            Rating rate25 = new Rating(RandomLongGenerator.generateRandomLong(), 1,2.5);
            ratingRepositoryNeo4J.create(rate25);
        }

        if(ratingRepositoryNeo4J.findByRate(3.0).isEmpty()) {
            Rating rate3 = new Rating(RandomLongGenerator.generateRandomLong(), 1,3.0);
            ratingRepositoryNeo4J.create(rate3);
        }

        if(ratingRepositoryNeo4J.findByRate(3.5).isEmpty()) {
            Rating rate35 = new Rating(RandomLongGenerator.generateRandomLong(), 1,3.5);
            ratingRepositoryNeo4J.create(rate35);
        }

        if(ratingRepositoryNeo4J.findByRate(4.0).isEmpty()) {
            Rating rate4 = new Rating(RandomLongGenerator.generateRandomLong(), 1,4.0);
            ratingRepositoryNeo4J.create(rate4);
        }

        if(ratingRepositoryNeo4J.findByRate(4.5).isEmpty()) {
            Rating rate45 = new Rating(RandomLongGenerator.generateRandomLong(), 1,4.5);
            ratingRepositoryNeo4J.create(rate45);
        }

        if(ratingRepositoryNeo4J.findByRate(5.0).isEmpty()) {
            Rating rate5 = new Rating(RandomLongGenerator.generateRandomLong(), 1,5.0);
            ratingRepositoryNeo4J.create(rate5);
        }
    }
}
