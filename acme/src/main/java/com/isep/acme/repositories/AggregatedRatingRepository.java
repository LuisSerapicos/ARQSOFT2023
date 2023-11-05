package com.isep.acme.repositories;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;
import com.isep.acme.repositories.databases.AggregatedRatingDataBase;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class AggregatedRatingRepository implements AggregatedRatingDataBase {
    @Override
    public Optional<AggregatedRating> findByProductId(Product product) {
        return null;
    }

    @Override
    public AggregatedRating create(AggregatedRating rating) {
        return null;
    }
}
