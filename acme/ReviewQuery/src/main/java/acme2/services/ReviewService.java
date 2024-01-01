package acme2.services;

import acme2.model.*;

import java.util.List;

public interface ReviewService {

    Iterable<Review> getAll();

    List<ReviewDTO> getReviewsOfProduct(String sku, String status);

    Iterable<ReviewDTO> getRecommendedReviews(Long userId);

    ReviewDTO create(CreateReviewDTO createReviewDTO, String sku);

    Review createReview(Review review);

    Product createProduct(Product product);

    boolean addVoteToReview(Long reviewID, VoteReviewDTO voteReviewDTO);

    Double getWeightedAverage(Product product);

    Boolean DeleteReview(Long reviewId);

    List<ReviewDTO> findPendingReview();

    ReviewDTO moderateReview(Long reviewID, String approved);

    List<ReviewDTO> findReviewsByUser(Long userID);
}
