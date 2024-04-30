package acme.review;

import acme.model.ReviewDTO;

public interface ReviewGenerator {
    Iterable<ReviewDTO> getRecommendedReviews(Long userId);
}
