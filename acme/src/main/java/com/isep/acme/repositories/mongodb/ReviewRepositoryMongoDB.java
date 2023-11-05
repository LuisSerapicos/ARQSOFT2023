package com.isep.acme.repositories.mongodb;

import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.User;
import com.isep.acme.persistance.mongodb.ProductMongo;
import com.isep.acme.persistance.mongodb.ReviewMongo;
import com.isep.acme.repositories.databases.ProductDataBase;
import com.isep.acme.repositories.databases.RatingDataBase;
import com.isep.acme.repositories.databases.ReviewDataBase;
import com.isep.acme.repositories.databases.UserDataBase;
import com.isep.acme.utils.ConvertIterable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("mongoDB3")
public class ReviewRepositoryMongoDB implements ReviewDataBase {

    private final MongoTemplate mongoTemplate;

    private final ProductDataBase productDataBase;

    private final UserDataBase userDataBase;

    private final RatingDataBase ratingDataBase;

    @Autowired
    public ReviewRepositoryMongoDB(MongoTemplate mongoTemplate, @Value("${database.interface.default}") String beanName2, @Value("${user.interface.default}") String beanName3, @Value("${rating.interface.default}")String beanName4, ApplicationContext context) {
        this.mongoTemplate = mongoTemplate;
        this.productDataBase = context.getBean(beanName2, ProductDataBase.class);
        this.userDataBase = context.getBean(beanName3, UserDataBase.class);
        this.ratingDataBase = context.getBean(beanName4, RatingDataBase.class);
    }

    @Override
    public Iterable<Review> getAllReview() {
        Query query = new Query();
        List<ReviewMongo> reviewMongo = mongoTemplate.find(query, ReviewMongo.class);
        if (reviewMongo.isEmpty()) {
            System.out.println("Product don't exist");
            return null;
        }
        Iterable<ReviewMongo> productIterableMongo = new ConvertIterable<>(reviewMongo);
        List<Review> review = new ArrayList<>();
        for (ReviewMongo pd : productIterableMongo) {
            review.add(pd.toReview());
        }
        return review;
    }

    @Override
    public Review create(Review review) {
        if (review == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User user = review.getUser();
        User user2 = userDataBase.getById(user.getUserId());
        review.setUser(user2);
        Product product = review.getProduct();
        Query query = new Query(Criteria.where("sku").is(product.sku));
        ProductMongo product2 = mongoTemplate.findOne(query, ProductMongo.class);
        review.setProduct(product2.toProduct());
        ReviewMongo save = toReviewMongoCreate(review);
        mongoTemplate.save(save);
        return save.toReview();
    }

    @Override
    public Review updateApprovalStatus(Review review) {
        if (review == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        ReviewMongo save = toReviewMongoUpdate(review);
        if (findById(save.getIdReview()).isPresent()) {
            Query query = new Query(Criteria.where("idReview").is(save.getIdReview()));
            Update update = new Update();
            update.set("approvalStatus", review.getApprovalStatus());

            mongoTemplate.updateFirst(query, update, ReviewMongo.class);
            return review;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public Review updateVote(Review review, String voteType) {
            ReviewMongo save = toReviewMongoUpdate(review);
            if (findById(save.getIdReview()).isPresent()) {
                Query query = new Query(Criteria.where("idReview").is(save.getIdReview()));
                Update update = new Update();
                if(voteType.equalsIgnoreCase("upVote")){
                    save.setUpVote(review.getUpVote());
                    update.set(voteType, save.getUpVote());
                }else{
                    save.setDownVote(review.getDownVote());
                    update.set(voteType, save.getDownVote());
                }
                mongoTemplate.updateFirst(query, update, ReviewMongo.class);
                return review;
            }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public Optional<List<Review>> findByProductIdStatus(Product product, String status) {
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where("product.sku").is(product.sku),
                Criteria.where("approvalStatus").is(status)
        );
        Query query = new Query(criteria);
        List<ReviewMongo> reviewMongo = mongoTemplate.find(query, ReviewMongo.class);
        if (reviewMongo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List<Review> review = new ArrayList<>();
        for (ReviewMongo pd : reviewMongo) {
            review.add(pd.toReview());
        }
        return Optional.of(review);
    }

    @Override
    public Optional<Review> findById(Long reviewID) {
        Query query = new Query(Criteria.where("idReview").is(reviewID));
        ReviewMongo reviewMongo = mongoTemplate.findOne(query, ReviewMongo.class);
        if (reviewMongo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return Optional.of(reviewMongo.toReview());
    }

    @Override
    public Optional<List<Review>> findByProductId(Product product) {
        Query query = new Query(Criteria.where("product.productID").is(product.getProductID()));
        List<ReviewMongo> reviewMongo = mongoTemplate.find(query, ReviewMongo.class);
        if (reviewMongo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List<Review> review = new ArrayList<>();
        for (ReviewMongo pd : reviewMongo) {
            review.add(pd.toReview());
        }
        return Optional.of(review);
    }

    @Override
    public void delete(Review r) {
        Query query = new Query(Criteria.where("idReview").is(r.getIdReview()));
        mongoTemplate.remove(query, ReviewMongo.class);
    }

    @Override
    public Optional<List<Review>> findPendingReviews() {
        Query query = new Query(Criteria.where("approvalStatus").is("pending"));
        List<ReviewMongo> reviewMongo = mongoTemplate.find(query, ReviewMongo.class);
        if (reviewMongo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List<Review> review = new ArrayList<>();
        for (ReviewMongo pd : reviewMongo) {
            review.add(pd.toReview());
        }
        return Optional.of(review);
    }

    @Override
    public Optional<List<Review>> findByUserId(User user) {
        Query query = new Query(Criteria.where("user.userId").is(user.getUserId()));
        List<ReviewMongo> reviewMongo = mongoTemplate.find(query, ReviewMongo.class);
        if (reviewMongo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List<Review> review = new ArrayList<>();
        for (ReviewMongo pd : reviewMongo) {
            review.add(pd.toReview());
        }
        return Optional.of(review);
    }

    private ReviewMongo toReviewMongo(Review review) {
        return new ReviewMongo(review.getVersion(), review.getApprovalStatus(), review.getReviewText(), review.getUpVote(), review.getDownVote(), review.getReport(), review.getPublishingDate(), review.getFunFact(), productDataBase.toProductMongo(review.getProduct()), userDataBase.toUserMongo(review.getUser()), ratingDataBase.toRatingMongo(review.getRating()));
    }

    private ReviewMongo toReviewMongoCreate(Review review) {
        return new ReviewMongo(review.getVersion(), review.getApprovalStatus(), review.getReviewText(), review.getUpVote(), review.getDownVote(), review.getReport(), review.getPublishingDate(), review.getFunFact(), productDataBase.toProductMongo(review.getProduct()), userDataBase.toUserMongo(review.getUser()), ratingDataBase.toRatingMongo(review.getRating()));
    }

    private ReviewMongo toReviewMongoUpdate(Review review){
        return new ReviewMongo(review.getIdReview(),review.getVersion(), review.getApprovalStatus(), review.getReviewText(), review.getUpVote(), review.getDownVote(), review.getReport(), review.getPublishingDate(), review.getFunFact(), productDataBase.toProductMongo(review.getProduct()), userDataBase.toUserMongo(review.getUser()), ratingDataBase.toRatingMongo(review.getRating()));
    }
}
