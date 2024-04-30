package acme.services;

import acme.model.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReviewService {

    Iterable<Review> getAll();

    List<ReviewDTO> getReviewsOfProduct(String sku, String status);

    Iterable<ReviewDTO> getRecommendedReviews(Long userId);

    ReviewDTO create(CreateReviewDTO createReviewDTO, String sku);

    Product createProduct(Product product);

    User createUser(String userID);

    ResponseEntity<String> checkIfUserExist(String userID);

    boolean addVoteToReview(Long reviewID, VoteReviewDTO voteReviewDTO);

    Double getWeightedAverage(Product product);

    Boolean DeleteReview(Long reviewId);

    List<ReviewDTO> findPendingReview();

    ReviewDTO moderateReview(Long reviewID, String approved);

    List<ReviewDTO> findReviewsByUser(Long userID);
}
