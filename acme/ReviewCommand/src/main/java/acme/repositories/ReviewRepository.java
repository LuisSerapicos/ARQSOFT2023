package acme.repositories;

import acme.model.Product;
import acme.model.Review;
import acme.model.User;
import acme.repositories.databases.ReviewDataBase;

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
    public Product createProduct(Product product) {
        return null;
    }

    @Override
    public User createUser(User user) {
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

    @Override
    public Optional<List<Review>> findByApprovalStatus(String status) {
        return Optional.empty();
    }
}
