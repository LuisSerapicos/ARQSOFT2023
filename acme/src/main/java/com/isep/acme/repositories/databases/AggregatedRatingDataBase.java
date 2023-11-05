package com.isep.acme.repositories.databases;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;

import java.util.Optional;

public interface AggregatedRatingDataBase {
    Optional<AggregatedRating> findByProductId(Product product);

    AggregatedRating create(AggregatedRating rating);
}
