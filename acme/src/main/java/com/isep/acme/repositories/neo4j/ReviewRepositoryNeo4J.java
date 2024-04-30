package com.isep.acme.repositories.neo4j;

import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.model.User;
import com.isep.acme.model.Vote;
import com.isep.acme.persistance.neo4j.*;
import com.isep.acme.repositories.databases.ProductDataBase;
import com.isep.acme.repositories.databases.RatingDataBase;
import com.isep.acme.repositories.databases.ReviewDataBase;
import com.isep.acme.repositories.databases.UserDataBase;
import com.isep.acme.utils.ConvertIterable;
import com.isep.acme.utils.RandomLongGenerator;
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

    private final RatingDataBase ratingDataBase;

    @Autowired
    public ReviewRepositoryNeo4J(@Value("${database.interface.default}") String beanName2, @Value("${user.interface.default}") String beanName3, @Value("${rating.interface.default}") String beanName4, ApplicationContext context, Session session) {
        this.session = session;
        this.productDataBase = context.getBean(beanName2, ProductDataBase.class);
        this.userDataBase = context.getBean(beanName3, UserDataBase.class);
        this.ratingDataBase = context.getBean(beanName4, RatingDataBase.class);
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
        User user2 = userDataBase.getById(user.getUserId());

        review.setUser(user2);

        Long randomId = RandomLongGenerator.generateRandomLong();
        review.setIdReview(randomId);

        ReviewNeo4J save = toReviewNeo4J(review);
        save.setId(randomId);

        session.save(save);
        return review;
    }

    @Override
    public Review updateApprovalStatus(Review review) {
        if (review == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        ReviewNeo4J save = toReviewNeo4J(review);
        if(findById(save.getIdReview()).isPresent()) {
            Optional<Review> r = findById(save.getIdReview());

            //r.get().setApprovalStatus("approved");

            String cypherQuery = "MATCH (p:ReviewNeo4J {idReview: $reviewID}) SET p.approvalStatus = $status WITH p " +
                    "OPTIONAL MATCH (p)-[:PRODUCT]->(product:ProductNeo4J)\n" +
                    "OPTIONAL MATCH (p)-[:USER]->(user:UserNeo4J)\n" +
                    "OPTIONAL MATCH (p)-[:RATING]->(rating:RatingNeo4J)\n" +
                    "OPTIONAL MATCH (p)-[:UP_VOTE]->(upvote:VoteNeo4J)\n" +
                    "OPTIONAL MATCH (p)-[:DOWN_VOTE]->(downvote:VoteNeo4J)\n" +
                    "RETURN p, product, user, rating, COLLECT(upvote) AS upvotes, COLLECT(downvote) AS downvotes";
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("reviewID", r.get().getIdReview());
            parameters.put("status", review.getApprovalStatus());
            Result result = session.query(cypherQuery, parameters);

            for (Map<String, Object> row : result) {
                ReviewNeo4J reviewNeo4J = (ReviewNeo4J) row.get("p");
                ProductNeo4J productNeo4J = (ProductNeo4J) row.get("product");
                UserNeo4J userNeo4J = (UserNeo4J) row.get("user");
                RatingNeo4J ratingNeo4J = (RatingNeo4J) row.get("rating");

                reviewNeo4J.setProduct(productNeo4J);
                reviewNeo4J.setUser(userNeo4J);
                reviewNeo4J.setRating(ratingNeo4J);
                reviewNeo4J.setApprovalStatus(review.getApprovalStatus());

                List<VoteNeo4J> upVoteNeo4JList = new ArrayList<>();
                List<VoteNeo4J> downVoteNeo4JList = new ArrayList<>();

                Object upvotesObj = row.get("upvotes");

                if (upvotesObj instanceof Iterable) {
                    for (Object item : (Iterable<?>) upvotesObj) {
                        upVoteNeo4JList.add((VoteNeo4J) item);
                    }
                }

                Object downvotesObj = row.get("downvotes");

                if (downvotesObj instanceof Iterable) {
                    for (Object item : (Iterable<?>) downvotesObj) {
                        downVoteNeo4JList.add((VoteNeo4J) item);
                    }
                }

                reviewNeo4J.setUpVote(upVoteNeo4JList);
                reviewNeo4J.setDownVote(downVoteNeo4JList);

                reviewNeo4J.toReview();
            }

            return review;
        }
        return null;
    }

    @Override
    public Review updateVote(Review review, String voteType) {
        ReviewNeo4J save = toReviewNeo4J(review);

        String cypherQuery = "MATCH (p:ReviewNeo4J {idReview: $reviewID}) " +
                "OPTIONAL MATCH (p)-[:PRODUCT]->(product:ProductNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:USER]->(user:UserNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:RATING]->(rating:RatingNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:UP_VOTE]->(upvote:VoteNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:DOWN_VOTE]->(downvote:VoteNeo4J)\n" +
                "RETURN p, product, user, rating, upvote, downvote";
        Map<String, Object> parameters = Collections.singletonMap("reviewID", review.getIdReview());
        Result result = session.query(cypherQuery, parameters);

        if (findById(save.getIdReview()).isPresent()) {
            for (Map<String, Object> row : result) {
                ReviewNeo4J reviewNeo4J = (ReviewNeo4J) row.get("p");
                ProductNeo4J productNeo4J = (ProductNeo4J) row.get("product");
                UserNeo4J userNeo4J = (UserNeo4J) row.get("user");
                RatingNeo4J ratingNeo4J = (RatingNeo4J) row.get("rating");

                reviewNeo4J.setProduct(productNeo4J);
                reviewNeo4J.setUser(userNeo4J);
                reviewNeo4J.setRating(ratingNeo4J);

                if (voteType.equalsIgnoreCase("upVote")) {
                    for(Vote v : review.getUpVote()) {
                        reviewNeo4J.addUpVote(toVoteNeo4J(v));
                        session.save(reviewNeo4J);
                    }
                    reviewNeo4J.toReview();
                } else {
                    for(Vote v : review.getDownVote()) {
                        reviewNeo4J.addDownVote(toVoteNeo4J(v));
                        session.save(reviewNeo4J);
                    }
                    reviewNeo4J.toReview();
                }
            }
            return review;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public Optional<List<Review>> findByProductIdStatus(Product product, String status) {
        String cypherQuery = "MATCH (p:ProductNeo4J {sku: $sku})<-[:PRODUCT]-(r:ReviewNeo4J {approvalStatus: $status}) " +
                "OPTIONAL MATCH (r)-[:PRODUCT]->(product:ProductNeo4J)\n" +
                "OPTIONAL MATCH (r)-[:USER]->(user:UserNeo4J)\n" +
                "OPTIONAL MATCH (r)-[:RATING]->(rating:RatingNeo4J)\n" +
                "OPTIONAL MATCH (r)-[:UP_VOTE]->(upvote:VoteNeo4J)\n" +
                "OPTIONAL MATCH (r)-[:DOWN_VOTE]->(downvote:VoteNeo4J)\n" +
                "RETURN r, product, user, rating, COLLECT(upvote) AS upvotes, COLLECT(downvote) AS downvotes";
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

            List<VoteNeo4J> upVoteNeo4JList = new ArrayList<>();
            List<VoteNeo4J> downVoteNeo4JList = new ArrayList<>();

            Object upvotesObj = row.get("upvotes");

            if (upvotesObj instanceof Iterable) {
                for (Object item : (Iterable<?>) upvotesObj) {
                    upVoteNeo4JList.add((VoteNeo4J) item);
                }
            }

            Object downvotesObj = row.get("downvotes");

            if (downvotesObj instanceof Iterable) {
                for (Object item : (Iterable<?>) downvotesObj) {
                    downVoteNeo4JList.add((VoteNeo4J) item);
                }
            }

            reviewNeo4J.setUpVote(upVoteNeo4JList);
            reviewNeo4J.setDownVote(downVoteNeo4JList);

            reviews.add(reviewNeo4J.toReview());
        }

        if(!reviews.isEmpty())
            return Optional.of(reviews);
        else
            return Optional.empty();
    }

    @Override
    public Optional<Review> findById(Long reviewID) {
        String cypherQuery = "MATCH (p:ReviewNeo4J {idReview: $reviewID}) " +
                "OPTIONAL MATCH (p)-[:PRODUCT]->(product:ProductNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:USER]->(user:UserNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:RATING]->(rating:RatingNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:UP_VOTE]->(upvote:VoteNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:DOWN_VOTE]->(downvote:VoteNeo4J)\n" +
                "RETURN p, product, user, rating, COLLECT(upvote) AS upvotes, COLLECT(downvote) AS downvotes";
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

            List<VoteNeo4J> upVoteNeo4JList = new ArrayList<>();
            List<VoteNeo4J> downVoteNeo4JList = new ArrayList<>();

            Object upvotesObj = row.get("upvotes");

            if (upvotesObj instanceof Iterable) {
                for (Object item : (Iterable<?>) upvotesObj) {
                    upVoteNeo4JList.add((VoteNeo4J) item);
                }
            }

            Object downvotesObj = row.get("downvotes");

            if (downvotesObj instanceof Iterable) {
                for (Object item : (Iterable<?>) downvotesObj) {
                    downVoteNeo4JList.add((VoteNeo4J) item);
                }
            }

            reviewNeo4J.setUpVote(upVoteNeo4JList);
            reviewNeo4J.setDownVote(downVoteNeo4JList);

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
                "OPTIONAL MATCH (r)-[:UP_VOTE]->(upvote:VoteNeo4J)\n" +
                "OPTIONAL MATCH (r)-[:DOWN_VOTE]->(downvote:VoteNeo4J)\n" +
                "RETURN r, product, user, rating, COLLECT(upvote) AS upvotes, COLLECT(downvote) AS downvotes";
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

            List<VoteNeo4J> upVoteNeo4JList = new ArrayList<>();
            List<VoteNeo4J> downVoteNeo4JList = new ArrayList<>();

            Object upvotesObj = row.get("upvotes");

            if (upvotesObj instanceof Iterable) {
                for (Object item : (Iterable<?>) upvotesObj) {
                    upVoteNeo4JList.add((VoteNeo4J) item);
                }
            }

            Object downvotesObj = row.get("downvotes");

            if (downvotesObj instanceof Iterable) {
                for (Object item : (Iterable<?>) downvotesObj) {
                    downVoteNeo4JList.add((VoteNeo4J) item);
                }
            }

            reviewNeo4J.setUpVote(upVoteNeo4JList);
            reviewNeo4J.setDownVote(downVoteNeo4JList);

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
                + " OPTIONAL MATCH (p)-[:PRODUCT]->(product:ProductNeo4J)"
                + " OPTIONAL MATCH (p)-[:RATING]->(rating:RatingNeo4J)"
                + " OPTIONAL MATCH (p)-[:USER]->(user:UserNeo4J)"
                + " DETACH DELETE p, product, rating, user";
        Map<String, Object> parameters = Collections.singletonMap("idReview", r.getIdReview());
        session.query(cypherQuery, parameters);
    }

    @Override
    public Optional<List<Review>> findPendingReviews() {
        String cypherQuery = "MATCH (p:ReviewNeo4J {approvalStatus: 'pending'})" +
                "OPTIONAL MATCH (p)-[:PRODUCT]->(product:ProductNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:USER]->(user:UserNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:RATING]->(rating:RatingNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:UP_VOTE]->(upvote:VoteNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:DOWN_VOTE]->(downvote:VoteNeo4J)\n" +
                "RETURN p, product, user, rating, COLLECT(upvote) AS upvotes, COLLECT(downvote) AS downvotes";
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

            List<VoteNeo4J> upVoteNeo4JList = new ArrayList<>();
            List<VoteNeo4J> downVoteNeo4JList = new ArrayList<>();

            Object upvotesObj = row.get("upvotes");

            if (upvotesObj instanceof Iterable) {
                for (Object item : (Iterable<?>) upvotesObj) {
                    upVoteNeo4JList.add((VoteNeo4J) item);
                }
            }

            Object downvotesObj = row.get("downvotes");

            if (downvotesObj instanceof Iterable) {
                for (Object item : (Iterable<?>) downvotesObj) {
                    downVoteNeo4JList.add((VoteNeo4J) item);
                }
            }

            reviewNeo4J.setUpVote(upVoteNeo4JList);
            reviewNeo4J.setDownVote(downVoteNeo4JList);

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
                "OPTIONAL MATCH (r)-[:UP_VOTE]->(upvote:VoteNeo4J)\n" +
                "OPTIONAL MATCH (r)-[:DOWN_VOTE]->(downvote:VoteNeo4J)\n" +
                "RETURN r, product, user, rating, COLLECT(upvote) AS upvotes, COLLECT(downvote) AS downvotes";
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

            List<VoteNeo4J> upVoteNeo4JList = new ArrayList<>();
            List<VoteNeo4J> downVoteNeo4JList = new ArrayList<>();

            Object upvotesObj = row.get("upvotes");

            if (upvotesObj instanceof Iterable) {
                for (Object item : (Iterable<?>) upvotesObj) {
                    upVoteNeo4JList.add((VoteNeo4J) item);
                }
            }

            Object downvotesObj = row.get("downvotes");

            if (downvotesObj instanceof Iterable) {
                for (Object item : (Iterable<?>) downvotesObj) {
                    downVoteNeo4JList.add((VoteNeo4J) item);
                }
            }

            reviewNeo4J.setUpVote(upVoteNeo4JList);
            reviewNeo4J.setDownVote(downVoteNeo4JList);

            reviews.add(reviewNeo4J.toReview());
        }

        if(!reviews.isEmpty())
            return Optional.of(reviews);
        else
            return Optional.empty();
    }

    @Override
    public Optional<List<Review>> findByApprovalStatus(String status) {
        String cypherQuery = "MATCH (p:ReviewNeo4J {approvalStatus: $status}) " +
                "OPTIONAL MATCH (p)-[:PRODUCT]->(product:ProductNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:USER]->(user:UserNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:RATING]->(rating:RatingNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:UP_VOTE]->(upvote:VoteNeo4J)\n" +
                "OPTIONAL MATCH (p)-[:DOWN_VOTE]->(downvote:VoteNeo4J)\n" +
                "RETURN p, product, user, rating, COLLECT(upvote) AS upvotes, COLLECT(downvote) AS downvotes";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("status", status);
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

            List<VoteNeo4J> upVoteNeo4JList = new ArrayList<>();
            List<VoteNeo4J> downVoteNeo4JList = new ArrayList<>();

            Object upvotesObj = row.get("upvotes");

            if (upvotesObj instanceof Iterable) {
                for (Object item : (Iterable<?>) upvotesObj) {
                    upVoteNeo4JList.add((VoteNeo4J) item);
                }
            }

            Object downvotesObj = row.get("downvotes");

            if (downvotesObj instanceof Iterable) {
                for (Object item : (Iterable<?>) downvotesObj) {
                    downVoteNeo4JList.add((VoteNeo4J) item);
                }
            }

            reviewNeo4J.setUpVote(upVoteNeo4JList);
            reviewNeo4J.setDownVote(downVoteNeo4JList);

            reviews.add(reviewNeo4J.toReview());
        }

        if(!reviews.isEmpty())
            return Optional.of(reviews);
        else
            return Optional.empty();
    }

    public ReviewNeo4J toReviewNeo4J(Review review) {
        return new ReviewNeo4J(review.getIdReview(), review.getVersion(), review.getApprovalStatus(), review.getReviewText(), toVoteNeo4JList(review.getUpVote()), toVoteNeo4JList(review.getDownVote()), "report", review.getPublishingDate(), review.getFunFact(), productDataBase.toProductNeo4J(review.getProduct()), ratingDataBase.toRatingNeo4J(review.getRating()), userDataBase.toUserNeo4J(review.getUser()));
    }

    public List<VoteNeo4J> toVoteNeo4JList(List<Vote> vote) {
        List<VoteNeo4J> voteNeo4JList = new ArrayList<>();

        for(Vote v : vote) {
            voteNeo4JList.add(toVoteNeo4J(v));
        }

        return voteNeo4JList;
    }

    public VoteNeo4J toVoteNeo4J(Vote vote) {
        return new VoteNeo4J(vote.getVote(), vote.getUserID());
    }
}