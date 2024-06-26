package acme.repositories.databases;

import acme.model.Product;
import acme.model.Review;
import acme.model.User;

import java.util.List;
import java.util.Optional;

public interface ReviewDataBase {
    Iterable<Review> getAllReview();

    Review create(Review review);

    Product createProduct(Product product);

    User createUser(User user);

    Review updateApprovalStatus(Review review);

    Review updateVote(Review review, String voteType);

    Optional<List<Review>> findByProductIdStatus(Product product, String status);

    Optional<Review> findById(Long reviewID);

    Optional<List<Review>> findByProductId(Product product);

    void delete(Review r);

    Optional<List<Review>> findPendingReviews();

    Optional<List<Review>> findByUserId(User user);

    Optional<List<Review>> findByApprovalStatus(String status);
}
