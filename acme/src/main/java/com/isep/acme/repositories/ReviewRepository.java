package com.isep.acme.repositories;

import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.User;
import com.isep.acme.repositories.databases.ReviewDataBase;

import java.util.List;
import java.util.Optional;

public class ReviewRepository implements ReviewDataBase {

    //Optional<Review> findById(Long productId);
    @Override
    public Iterable<Review> getAllReview() {
        return null;
    }

    @Override
    public Review create(Review review) {
        return null;
    }

    @Override
    public Review updateApprovalStatus(Review review) {
        return null;
    }

    @Override
    public Review updateVote(Review review, String voteType) {
        return null;
    }

    @Override
    public Optional<List<Review>> findByProductIdStatus(Product product, String status) {
        return Optional.empty();
    }

    @Override
    public Optional<Review> findById(Long reviewID) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Review>> findByProductId(Product product) {
        return Optional.empty();
    }

    @Override
    public void delete(Review r) {

    }

    @Override
    public Optional<List<Review>> findPendingReviews() {
        return Optional.empty();
    }

    @Override
    public Optional<List<Review>> findByUserId(User user) {
        return Optional.empty();
    }
}
