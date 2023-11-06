package com.isep.acme.review;

import com.isep.acme.model.ReviewDTO;

public interface ReviewGenerator {
    Iterable<ReviewDTO> getRecommendedReviews(Long userId);
}
