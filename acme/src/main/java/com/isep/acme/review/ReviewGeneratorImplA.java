package com.isep.acme.review;

import com.isep.acme.model.Review;
import com.isep.acme.model.ReviewDTO;
import com.isep.acme.model.ReviewMapper;
import com.isep.acme.repositories.databases.ReviewDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("reviewA")
public class ReviewGeneratorImplA implements ReviewGenerator{
    private final ReviewDataBase reviewDataBase;

    @Autowired
    public ReviewGeneratorImplA(@Value("${review.interface.default}") String beanName, ApplicationContext context) {
        this.reviewDataBase = context.getBean(beanName, ReviewDataBase.class);
    }

    @Override
    public Iterable<ReviewDTO> getRecommendedReviews(Long userId) {
        final Optional<List<Review>> allReviews = reviewDataBase.findByApprovalStatus("approved");  //Find all the reviews with the status = approved

        // Create a list to store reviews that meet the criteria
        List<ReviewDTO> selectedReviews = new ArrayList<>();

        if (allReviews.isEmpty()) {
            return selectedReviews; // Return the empty list
        }

        //Loop through all the accepted reviews
        for (Review r : allReviews.get()) {
            int votes = r.getUpVote().size() + r.getDownVote().size();

            if (votes < 4) {
                System.out.println("Not enough votes");
                continue;
            }

            //Check the percentage of upvotes in the review
            double upVotePercentage = ((double) r.getUpVote().size() / votes) * 100;
            System.out.println("Percentage: " + upVotePercentage);
            if (upVotePercentage >= 60) {
                selectedReviews.add(ReviewMapper.toDto(r));
            }
        }

        return selectedReviews; // Return the list containing selected reviews
    }
}
