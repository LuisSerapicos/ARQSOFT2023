package acme.services;

import acme.controllers.ResourceNotFoundException;
import acme.model.*;
import acme.repositories.databases.ProductDataBase;
import acme.repositories.databases.ReviewDataBase;
import acme.repositories.databases.UserDataBase;
import acme.review.ReviewGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.acme.RabbitMQMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDataBase reviewDataBase;
    private final ProductDataBase productDataBase;
    private final UserDataBase userDataBase;
    private final ReviewGenerator reviewGenerator;

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
    public ReviewServiceImpl(@Value("${review.interface.default}") String beanName, @Value("${recommendation.alg}") String reviewBeanName, @Value("${database.interface.default}") String beanName2, @Value("${user.interface.default}") String beanName3, ApplicationContext context, RabbitMQMessageProducer rabbitMQMessageProducer) {
        this.rabbitMQMessageProducer = rabbitMQMessageProducer;
        this.reviewDataBase = context.getBean(beanName, ReviewDataBase.class);
        this.productDataBase = context.getBean(beanName2, ProductDataBase.class);
        this.userDataBase = context.getBean(beanName3, UserDataBase.class);
        this.reviewGenerator = context.getBean(reviewBeanName, ReviewGenerator.class);
    }

    @Override
    public Iterable<ReviewDTO> getRecommendedReviews(Long userId)
    {
        final Optional<User> user = userDataBase.findById(userId);

        if(user.isEmpty()) return null;

        Optional<List<Review>> r = reviewDataBase.findByUserId(user.get());

        if (r.isEmpty()) return null;

        return reviewGenerator.getRecommendedReviews(userId);
    }

    @Override
    public ReviewDTO create(final CreateReviewDTO createReviewDTO, String sku) {
        final Optional<Product> product = productDataBase.findBySku(sku);

        if (product.isEmpty()) return null;

        final var user = userDataBase.findByUsername(createReviewDTO.getUsername());

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
    public Product createProduct(Product product) {
        reviewDataBase.createProduct(product);

        return product;
    }

    @Override
    public User createUser(String userID) {
        ResponseEntity<String> response = checkIfUserExist(userID.toString());

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                User user = objectMapper.readValue(responseBody, User.class);
                System.out.println(user);
                reviewDataBase.createUser(user);
                return user;
            } catch (Exception e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
        }
        return null;
    }

    public ResponseEntity<String> checkIfUserExist(String userID){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "http://localhost:8083/api/v1/user/user";
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl + "/" + userID, String.class);
        System.out.println(response);
        return response;
    }

    @Override
    public List<ReviewDTO> getReviewsOfProduct(String sku, String status) {

        Optional<Product> product = productDataBase.findBySku(sku);
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

        final Optional<User> user = userDataBase.findById(userID);

        if (user.isEmpty()) return null;

        Optional<List<Review>> r = reviewDataBase.findByUserId(user.get());

        if (r.isEmpty()) return null;

        return ReviewMapper.toDtoList(r.get());
    }
}