package com.isep.acme.repositories;

import com.isep.acme.persistance.neo4j.ReviewNeo4J;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ReviewRepository extends Neo4jRepository<ReviewNeo4J, Long> {

    //Optional<Review> findById(Long productId);
    /*@Override
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
    public Optional<List<Review>> findByProductIdStatus(Long product, String status) {
        return Optional.empty();
    }

    @Override
    public Optional<Review> findById(Long reviewID) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Review>> findByProductId(Long product) {
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
    public Optional<List<Review>> findByUserId(Long user) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Review>> findByApprovalStatus(String status) {
        return Optional.empty();
    }*/
}
