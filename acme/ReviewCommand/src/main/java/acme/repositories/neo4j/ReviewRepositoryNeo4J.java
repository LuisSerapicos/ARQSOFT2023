package acme.repositories.neo4j;

import acme.model.Product;
import acme.model.Review;
import acme.model.User;
import acme.persistance.neo4j.ProductNeo4J;
import acme.persistance.neo4j.RatingNeo4J;
import acme.persistance.neo4j.ReviewNeo4J;
import acme.persistance.neo4j.UserNeo4J;
import acme.repositories.databases.ProductDataBase;
import acme.repositories.databases.ReviewDataBase;
import acme.repositories.databases.UserDataBase;
import acme.utils.ConvertIterable;
import acme.utils.RandomLongGenerator;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Component("neo4J3")
public class ReviewRepositoryNeo4J implements ReviewDataBase {

    private final Session session;

    private final ProductDataBase productDataBase;

    private final UserDataBase userDataBase;

    //private final RatingDataBase ratingDataBase;

    @Autowired
    public ReviewRepositoryNeo4J(@Value("${database.interface.default}") String beanName2, @Value("${user.interface.default}") String beanName3, ApplicationContext context, Session session) {
        this.session = session;
        this.productDataBase = context.getBean(beanName2, ProductDataBase.class);
        this.userDataBase = context.getBean(beanName3, UserDataBase.class);
        //this.ratingDataBase = context.getBean(beanName4, RatingDataBase.class);
    }

    @Override
    public Iterable<Review> getAllReview() {
        Collection<ReviewNeo4J> result = session.loadAll(ReviewNeo4J.class);
        System.out.println("Result: " + result);

        if (result == null) {
            throw(new ResponseStatusException(HttpStatus.NOT_FOUND));
        }

        Iterable<ReviewNeo4J> reviewNeo4J = new ConvertIterable<>(result);
        System.out.println("reviewsNeo4J: " + reviewNeo4J);
        List<Review> review = new ArrayList<>();
        for (ReviewNeo4J rv : reviewNeo4J) {
            review.add(rv.toReview());
            System.out.println("Product: " + review);
        }

        return review;
    }

    @Override
    public Review create(Review review) {

        User user = review.getUser();
        User user2 = userDataBase.findByUsername(user.getUsername()).get();

        review.setUser(user2);

        Long randomId = RandomLongGenerator.generateRandomLong();
        review.setIdReview(randomId);

        ReviewNeo4J save = toReviewNeo4J(review);
        save.setId(randomId);

        session.save(save);
        return review;
    }

    @Override
    public Product createProduct(Product product) {
        ProductNeo4J save = toProductNeo4J(product);

        session.save(save);
        return product;
    }

    @Override
    public User createUser(User user) {
        UserNeo4J save = toUserNeo4J(user);

        session.save(save);
        return user;
    }

    @Override
    public Review updateApprovalStatus(Review review) {
        if (review == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        ReviewNeo4J save = toReviewNeo4J(review);
        if(findById(save.getIdReview()).isPresent()) {
            Optional<Review> r = findById(save.getIdReview());

            r.get().setApprovalStatus("approved");

            String cypherQuery = "MATCH (p:ReviewNeo4J {idReview: $reviewID}) SET p.approvalStatus = 'approved' WITH p " +
                    "OPTIONAL MATCH (p)-[:PRODUCT]->(product:ProductNeo4J)\n" +
                    "OPTIONAL MATCH (p)-[:USER]->(user:UserNeo4J)\n" +
                    "OPTIONAL MATCH (p)-[:RATING]->(rating:RatingNeo4J)\n" +
                    "RETURN p, product, user, rating";
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("reviewID", r.get().getIdReview());
            Result result = session.query(cypherQuery, parameters);

            for (Map<String, Object> row : result) {
                ReviewNeo4J reviewNeo4J = (ReviewNeo4J) row.get("p");
                ProductNeo4J productNeo4J = (ProductNeo4J) row.get("product");
                UserNeo4J userNeo4J = (UserNeo4J) row.get("user");
                RatingNeo4J ratingNeo4J = (RatingNeo4J) row.get("rating");
                reviewNeo4J.setProduct(productNeo4J);
                reviewNeo4J.setUser(userNeo4J);
                reviewNeo4J.setRating(ratingNeo4J);

                reviewNeo4J.toReview();
            }

            return r.get();
        }
        return null;
    }

    @Override
    public Review updateVote(Review review, String voteType) {
        return null;
    }

    @Override
    public Optional<List<Review>> findByProductIdStatus(Product product, String status) {
        String cypherQuery = "MATCH (p:ProductNeo4J {sku: $sku})<-[:PRODUCT]-(r:ReviewNeo4J {approvalStatus: $status}) " +
                "OPTIONAL MATCH (r)-[:PRODUCT]->(product:ProductNeo4J)\n" +
                "OPTIONAL MATCH (r)-[:USER]->(user:UserNeo4J)\n" +
                "OPTIONAL MATCH (r)-[:RATING]->(rating:RatingNeo4J)\n" +
                "RETURN r, product, user, rating";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("sku", product.getSku());
        parameters.put("status", status);
        Result result = session.query(cypherQuery, parameters);

        List<Review> reviews = new ArrayList<>();

        for (Map<String, Object> row : result) {
            ReviewNeo4J reviewNeo4J = (ReviewNeo4J) row.get("r");
            ProductNeo4J productNeo4J = (ProductNeo4J) row.get("product");
            UserNeo4J userNeo4J = (UserNeo4J) row.get("user");
            RatingNeo4J ratingNeo4J = (RatingNeo4J) row.get("rating");
            reviewNeo4J.setProduct(productNeo4J);
            reviewNeo4J.setUser(userNeo4J);
            reviewNeo4J.setRating(ratingNeo4J);

            reviews.add(reviewNeo4J.toReview());
        }

        if(!reviews.isEmpty())
            return Optional.of(reviews);
        else
            return Optional.empty();
    }

    @Override
    public Optional<Review> findById(Long reviewID) {
        String cypherQuery = "MATCH (p:ReviewNeo4J {idReview: $reviewID})" +
                "OPTIONAL MATCH (p)-[:PRODUCT]->(product:ProductNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:USER]->(user:UserNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:RATING]->(rating:RatingNeo4J)\n" +
                "RETURN p, product, user, rating";
        Map<String, Object> parameters = Collections.singletonMap("reviewID", reviewID);
        Result result = session.query(cypherQuery, parameters);

        for (Map<String, Object> row : result) {
            ReviewNeo4J reviewNeo4J = (ReviewNeo4J) row.get("p");
            ProductNeo4J productNeo4J = (ProductNeo4J) row.get("product");
            UserNeo4J userNeo4J = (UserNeo4J) row.get("user");
            RatingNeo4J ratingNeo4J = (RatingNeo4J) row.get("rating");
            reviewNeo4J.setProduct(productNeo4J);
            reviewNeo4J.setUser(userNeo4J);
            reviewNeo4J.setRating(ratingNeo4J);

            return Optional.ofNullable((reviewNeo4J.toReview()));
        }

        return Optional.empty();
    }

    @Override
    public Optional<List<Review>> findByProductId(Product product) {
        String cypherQuery = "MATCH (p:ProductNeo4J {productId: $productId})<-[:PRODUCT]-(r:ReviewNeo4J) " +
                "OPTIONAL MATCH (r)-[:PRODUCT]->(product:ProductNeo4J)\n" +
                "OPTIONAL MATCH (r)-[:USER]->(user:UserNeo4J)\n" +
                "OPTIONAL MATCH (r)-[:RATING]->(rating:RatingNeo4J)\n" +
                "RETURN r, product, user, rating";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("productId", product.getProductID());
        Result result = session.query(cypherQuery, parameters);

        List<Review> reviews = new ArrayList<>();

        for (Map<String, Object> row : result) {
            ReviewNeo4J reviewNeo4J = (ReviewNeo4J) row.get("r");
            ProductNeo4J productNeo4J = (ProductNeo4J) row.get("product");
            UserNeo4J userNeo4J = (UserNeo4J) row.get("user");
            RatingNeo4J ratingNeo4J = (RatingNeo4J) row.get("rating");
            reviewNeo4J.setProduct(productNeo4J);
            reviewNeo4J.setUser(userNeo4J);
            reviewNeo4J.setRating(ratingNeo4J);

            reviews.add(reviewNeo4J.toReview());
        }

        if(!reviews.isEmpty())
            return Optional.of(reviews);
        else
            return Optional.empty();
    }

    @Override
    public void delete(Review r) {
        String cypherQuery = "MATCH (p:ReviewNeo4J {idReview: $idReview}) "
                + " OPTIONAL MATCH (r)-[:PRODUCT]->(product:ProductNeo4J)"
                + " OPTIONAL MATCH (r)-[:RATING]->(rating:RatingNeo4J)"
                + " OPTIONAL MATCH (r)-[:USER]->(user:UserNeo4J)"
                + " DETACH DELETE r, product, rating, user";
        Map<String, Object> parameters = Collections.singletonMap("idReview", r.getIdReview());
        session.query(cypherQuery, parameters);
    }

    @Override
    public Optional<List<Review>> findPendingReviews() {
        String cypherQuery = "MATCH (p:ReviewNeo4J {approvalStatus: 'pending'})" +
                "OPTIONAL MATCH (p)-[:PRODUCT]->(product:ProductNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:USER]->(user:UserNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:RATING]->(rating:RatingNeo4J)\n" +
                "RETURN p, product, user, rating";
        Map<String, Object> parameters = Collections.emptyMap();
        Result result = session.query(cypherQuery, parameters);

        List<Review> reviews = new ArrayList<>();

        for (Map<String, Object> row : result) {
            ReviewNeo4J reviewNeo4J = (ReviewNeo4J) row.get("p");
            ProductNeo4J productNeo4J = (ProductNeo4J) row.get("product");
            UserNeo4J userNeo4J = (UserNeo4J) row.get("user");
            RatingNeo4J ratingNeo4J = (RatingNeo4J) row.get("rating");
            reviewNeo4J.setProduct(productNeo4J);
            reviewNeo4J.setUser(userNeo4J);
            reviewNeo4J.setRating(ratingNeo4J);

            reviews.add(reviewNeo4J.toReview());
        }

        if(!reviews.isEmpty())
            return Optional.of(reviews);
        else
            return Optional.empty();
    }

    @Override
    public Optional<List<Review>> findByUserId(User user) {
        String cypherQuery = "MATCH (p:UserNeo4J {userId: $userId})<-[:USER]-(r:ReviewNeo4J) " +
                "OPTIONAL MATCH (r)-[:PRODUCT]->(product:ProductNeo4J)\n" +
                "OPTIONAL MATCH (r)-[:USER]->(user:UserNeo4J)\n" +
                "OPTIONAL MATCH (r)-[:RATING]->(rating:RatingNeo4J)\n" +
                "RETURN r, product, user, rating";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("userId", user.getUserId());
        Result result = session.query(cypherQuery, parameters);

        List<Review> reviews = new ArrayList<>();

        for (Map<String, Object> row : result) {
            ReviewNeo4J reviewNeo4J = (ReviewNeo4J) row.get("r");
            ProductNeo4J productNeo4J = (ProductNeo4J) row.get("product");
            UserNeo4J userNeo4J = (UserNeo4J) row.get("user");
            RatingNeo4J ratingNeo4J = (RatingNeo4J) row.get("rating");
            reviewNeo4J.setProduct(productNeo4J);
            reviewNeo4J.setUser(userNeo4J);
            reviewNeo4J.setRating(ratingNeo4J);

            reviews.add(reviewNeo4J.toReview());
        }

        if(!reviews.isEmpty())
            return Optional.of(reviews);
        else
            return Optional.empty();
    }

    @Override
    public Optional<List<Review>> findByApprovalStatus(String status) {
        String cypherQuery = "MATCH (p:ReviewNeo4J {approvalStatus: $status})" +
                "OPTIONAL MATCH (p)-[:PRODUCT]->(product:ProductNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:USER]->(user:UserNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:RATING]->(rating:RatingNeo4J)\n" +
                "RETURN p, product, user, rating";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("status", status);
        Result result = session.query(cypherQuery, parameters);

        List<Review> reviews = new ArrayList<>();

        for (Map<String, Object> row : result) {
            ReviewNeo4J reviewNeo4J = (ReviewNeo4J) row.get("r");
            ProductNeo4J productNeo4J = (ProductNeo4J) row.get("product");
            UserNeo4J userNeo4J = (UserNeo4J) row.get("user");
            RatingNeo4J ratingNeo4J = (RatingNeo4J) row.get("rating");
            reviewNeo4J.setProduct(productNeo4J);
            reviewNeo4J.setUser(userNeo4J);
            reviewNeo4J.setRating(ratingNeo4J);

            reviews.add(reviewNeo4J.toReview());
        }

        if(!reviews.isEmpty())
            return Optional.of(reviews);
        else
            return Optional.empty();
    }

    public ReviewNeo4J toReviewNeo4J(Review review) {
        return new ReviewNeo4J(review.getIdReview(), review.getVersion(), review.getApprovalStatus(), review.getReviewText(), review.getUpVote(), review.getDownVote(), "report", review.getPublishingDate(), review.getFunFact(), productDataBase.toProductNeo4J(review.getProduct()), new RatingNeo4J(4.5), userDataBase.toUserNeo4J(review.getUser()));
    }

    public ProductNeo4J toProductNeo4J(Product product) {
        return new ProductNeo4J(product.getProductID(), product.getSku(), product.getDesignation(), product.getDescription());
    }

    public UserNeo4J toUserNeo4J(User user) {
        return new UserNeo4J(user.getUserId(), user.getUsername(), user.getPassword(), user.getFullName(), user.getNif(), user.getMorada());
    }
}
