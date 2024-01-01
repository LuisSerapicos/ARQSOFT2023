package acme2.review;

import acme2.model.ReviewDTO;

public interface ReviewGenerator {
    Iterable<ReviewDTO> getRecommendedReviews(Long userId);
}
