package acme2.review;

import acme2.model.*;
import acme2.repositories.databases.ReviewDataBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component("reviewB")
public class ReviewGeneratorImplB implements ReviewGenerator{
    private final ReviewDataBase reviewDataBase;

    public ReviewGeneratorImplB(@Value("${review.interface.default}") String beanName, ApplicationContext context) {
        this.reviewDataBase = context.getBean(beanName, ReviewDataBase.class);;
    }

    @Override
    public Iterable<ReviewDTO> getRecommendedReviews(Long userId) {
        //Find all the reviews with the status = approved
        final Optional<List<Review>> allApprovedReviews = reviewDataBase.findByApprovalStatus("approved");

        //Return a user by the given id
        final Optional<User> user = Optional.of(new User(200L, "user4", "user4", "userquatro", "938233832", "Morada user4"));

        //Return all the reviews of the user
        Optional<List<Review>> allUserReviews = reviewDataBase.findByUserId(user.get());

        // Create a list to store reviews that meet the criteria
        List<ReviewDTO> selectedReviews = new ArrayList<>();

        if (allApprovedReviews.isEmpty()) {
            return selectedReviews; // Return the empty list
        }

        //Loop through all the accepted reviews
        for (Review r2 : allUserReviews.get()) {

            if (!r2.getApprovalStatus().equalsIgnoreCase("approved")) {
                return selectedReviews;
            }

            List<Vote> userUpVote = r2.getUpVote();
            List<Vote> userDownVote = r2.getDownVote();
            List<Vote> allVotes = new ArrayList<>();

            //Loop through all the upvotes and add them to a common votes array
            for(Vote upVote : userUpVote) {
                allVotes.add(upVote);
            }

            //Loop through all the downvotes and add them to a common votes array
            for(Vote downVote : userDownVote) {
                allVotes.add(downVote);
            }

            //Loop through all the votes
            for(Vote votes : allVotes) {
                //Find a user that voted
                //Bootstrap votes
                Optional<User> user2 = Optional.of(new User(300L, "user5", "user5", "usercinco", "938233832", "Morada user5"));

                //Find the user in the list of reviews
                Optional<List<Review>> allUserReviews2 = reviewDataBase.findByUserId(user2.get());

                //Loop through the reviews of the user that voted
                for (Review r3 : allUserReviews2.get()) {
                    int upVotes = r3.getUpVote().size() + r3.getDownVote().size();

                    //Check the percentage of upvotes in the review
                    double upVotePercentage = ((double) r3.getUpVote().size() / upVotes) * 100;
                    System.out.println("Percentage: " + upVotePercentage);
                    if (upVotePercentage >= 50) {
                        int index = 0;

                        if (!Objects.equals(selectedReviews.get(index).getIdReview(), r3.getIdReview())) {
                            selectedReviews.add(ReviewMapper.toDto(r3));
                        }
                        else
                            System.out.println("Review is already listed!");
                        index++;
                    }
                }
            }
        }

        return selectedReviews; // Return the list containing selected reviews
    }
}

//(2) Reviews de users que votam igual (ao utilizador que fez a review) cujos upvotes sejam 50%+
