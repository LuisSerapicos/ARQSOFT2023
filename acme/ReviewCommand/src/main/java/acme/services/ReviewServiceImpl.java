package acme.services;

import acme.model.*;
import acme.repositories.databases.ReviewDataBase;
import acme.controllers.ResourceNotFoundException;
import com.isep.acme.RabbitMQMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDataBase reviewDataBase;
    //private final ProductDataBase productDataBase;
    //private final UserDataBase userDataBase;
    //private final ReviewGenerator reviewGenerator;

    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    //@Autowired
    //UserService userService;

    //@Autowired
    //RatingService ratingService;

    @Autowired
    RestService restService;

    @Override
    public Iterable<Review> getAll() {
        return reviewDataBase.getAllReview();
    }

    @Autowired
    public ReviewServiceImpl(@Value("${review.interface.default}") String beanName, ApplicationContext context, RabbitMQMessageProducer rabbitMQMessageProducer) {
        this.rabbitMQMessageProducer = rabbitMQMessageProducer;
        this.reviewDataBase = context.getBean(beanName, ReviewDataBase.class);
        //this.productDataBase = context.getBean(beanName2, ProductDataBase.class);
        //this.userDataBase = context.getBean(beanName3, UserDataBase.class);
        //this.reviewGenerator = context.getBean(reviewBeanName, ReviewGenerator.class);
    }

    @Override
    public ReviewDTO create(final CreateReviewDTO createReviewDTO, String sku) {
        final Optional<Product> product = Optional.of(new Product(100L, "skumegafixe2", "designation2", "description2"));

        if (product.isEmpty()) return null;

        final var user = Optional.of(new User(100L, "user3", "user3", "usertres", "938233832", "Morada user3"));

        if (user.isEmpty()) return null;

        Rating rating = null;
        Optional<Rating> r = Optional.of(new Rating(4.0));
        if (r.isPresent()) {
            rating = r.get();
        }

        LocalDate date = LocalDate.now();

        String funfact = restService.getFunFact(date);

        if (funfact == null) return null;

        Review review = new Review(createReviewDTO.getUserID(), createReviewDTO.getReviewText(), date, product.get(), funfact, rating, user.get());

        review = reviewDataBase.create(review);

        review.setReport("report");

        if (review == null) return null;

        rabbitMQMessageProducer.publish(
                review,
                "internal.exchange",
                "internal.review.routing-key"
        );

        return ReviewMapper.toDto(review);
    }

    @Override
    public List<ReviewDTO> getReviewsOfProduct(String sku, String status) {

        Optional<Product> product = Optional.of(new Product("skumegafixe1"));
        if (product.isEmpty()) return null;

        Optional<List<Review>> r = reviewDataBase.findByProductIdStatus(product.get(), status);

        if (r.isEmpty()) return null;

        return ReviewMapper.toDtoList(r.get());
    }

    @Override
    public boolean addVoteToReview(Long reviewID, VoteReviewDTO voteReviewDTO) {

        Optional<Review> review = this.reviewDataBase.findById(reviewID);

        if (review.isEmpty()) return false;

        Vote vote = new Vote(voteReviewDTO.getVote(), voteReviewDTO.getUserID());
        if (voteReviewDTO.getVote().equalsIgnoreCase("upVote")) {
            boolean added = review.get().addUpVote(vote);
            if (added) {
                Review reviewUpdated = this.reviewDataBase.updateVote(review.get(), "upVote");
                return reviewUpdated != null;
            }
        } else if (voteReviewDTO.getVote().equalsIgnoreCase("downVote")) {
            boolean added = review.get().addDownVote(vote);
            if (added) {
                Review reviewUpdated = this.reviewDataBase.updateVote(review.get(), "downVote");
                return reviewUpdated != null;
            }
        }
        return false;
    }

    @Override
    public Double getWeightedAverage(Product product) {

        Optional<List<Review>> r = reviewDataBase.findByProductId(product);

        if (r.isEmpty()) return 0.0;

        double sum = 0;

        for (Review rev : r.get()) {
            Rating rate = rev.getRating();

            if (rate != null) {
                sum += rate.getRate();
            }
        }

        return sum / r.get().size();
    }

    @Override
    public Boolean DeleteReview(Long reviewId) {

        Optional<Review> rev = reviewDataBase.findById(reviewId);

        if (rev.isEmpty()) {
            return null;
        }
        Review r = rev.get();

        if (r.getUpVote().isEmpty() && r.getDownVote().isEmpty()) {
            reviewDataBase.delete(r);
            return true;
        }
        return false;
    }

    @Override
    public List<ReviewDTO> findPendingReview() {

        Optional<List<Review>> r = reviewDataBase.findPendingReviews();

        if (r.isEmpty()) {
            return null;
        }

        return ReviewMapper.toDtoList(r.get());
    }

    @Override
    public ReviewDTO moderateReview(Long reviewID, String approved) throws ResourceNotFoundException, IllegalArgumentException {

        Optional<Review> r = reviewDataBase.findById(reviewID);

        if (r.isEmpty()) {
            throw new ResourceNotFoundException("Review not found");
        }

        Boolean ap = r.get().setApprovalStatus(approved);

        if (!ap) {
            throw new IllegalArgumentException("Invalid status value");
        }

        Review review = reviewDataBase.updateApprovalStatus(r.get());

        return ReviewMapper.toDto(review);
    }


    @Override
    public List<ReviewDTO> findReviewsByUser(Long userID) {

        final Optional<User> user = Optional.of(new User("user1", "user1"));

        if (user.isEmpty()) return null;

        Optional<List<Review>> r = reviewDataBase.findByUserId(user.get());

        if (r.isEmpty()) return null;

        return ReviewMapper.toDtoList(r.get());
    }
}