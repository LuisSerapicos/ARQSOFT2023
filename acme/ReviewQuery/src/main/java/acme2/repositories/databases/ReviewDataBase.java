package acme2.repositories.databases;

import acme2.model.*;

import java.util.List;
import java.util.Optional;

public interface ReviewDataBase {
    Iterable<Review> getAllReview();

    Review create(Review review);

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
