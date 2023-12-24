package com.isep.acme.persistance.neo4j;

import com.isep.acme.model.Review;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Node("ReviewNeo4J")
public class ReviewNeo4J {

    @Id
    @GeneratedValue
    private Long id;

    private Long idReview;

    private long version;

    private String approvalStatus;

    private String reviewText;

    @Relationship(type = "UPVOTED_BY", direction = Relationship.Direction.INCOMING)
    private List<Long> upVote;

    @Relationship(type = "DOWNVOTED_BY", direction = Relationship.Direction.INCOMING)
    private List<Long> downVote;

    private String report;

    private LocalDate publishingDate;

    private String funFact;

    @Relationship(type = "FOR_PRODUCT", direction = Relationship.Direction.OUTGOING)
    private Long productId;

    @Relationship(type = "BY_USER", direction = Relationship.Direction.OUTGOING)
    private Long userId;

    @Relationship(type = "HAS_RATING", direction = Relationship.Direction.OUTGOING)
    private Long ratingId;

    protected ReviewNeo4J(){}

    public ReviewNeo4J(final Long idReview, final long version, final String approvalStatus, final String reviewText, final LocalDate publishingDate, final String funFact) {
        this.idReview = Objects.requireNonNull(idReview);
        this.version = Objects.requireNonNull(version);
        setApprovalStatus(approvalStatus);
        setReviewText(reviewText);
        setPublishingDate(publishingDate);
        setFunFact(funFact);
    }

    public ReviewNeo4J(final Long idReview, final long version, final String approvalStatus, final  String reviewText, final List<Long> upVote, final List<Long> downVote, final String report, final LocalDate publishingDate, final String funFact, Long product, Long rating, Long user) {
        this(idReview, version, approvalStatus, reviewText, publishingDate, funFact);

        setUpVote(upVote);
        setDownVote(downVote);
        setReport(report);
        setProduct(product);
        setRating(rating);
        setUser(user);

    }

    public ReviewNeo4J(final String reviewText, LocalDate publishingDate, Long product, String funFact, Long rating, Long user) {
        setReviewText(reviewText);
        setProduct(product);
        setPublishingDate(publishingDate);
        setApprovalStatus("pending");
        setFunFact(funFact);
        setRating(rating);
        setUser(user);
        this.upVote = new ArrayList<>();
        this.downVote = new ArrayList<>();
    }

    public Long getIdReview() {
        return idReview;
    }

    public void setId(Long id) {
        this.idReview = id;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public Boolean setApprovalStatus(String approvalStatus) {

        if( approvalStatus.equalsIgnoreCase("pending") ||
                approvalStatus.equalsIgnoreCase("approved") ||
                approvalStatus.equalsIgnoreCase("rejected")) {

            this.approvalStatus = approvalStatus;
            return true;
        }
        return false;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        if (reviewText == null || reviewText.isBlank()) {
            throw new IllegalArgumentException("Review Text is a mandatory attribute of Review.");
        }
        if (reviewText.length() > 2048) {
            throw new IllegalArgumentException("Review Text must not be greater than 2048 characters.");
        }

        this.reviewText = reviewText;
    }

    public void setReport(String report) {
        if (report.length() > 2048) {
            throw new IllegalArgumentException("Report must not be greater than 2048 characters.");
        }
        this.report = report;
    }

    public String getReport() {
        return report;
    }

    public LocalDate getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(LocalDate publishingDate) {
        this.publishingDate = publishingDate;
    }

    public long getVersion() {
        return version;
    }

    public String getFunFact() {
        return funFact;
    }

    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }

    public void setProduct(Long product) {
        this.productId = product;
    }

    public Long getProduct() {
        return productId;
    }

    public Long getUser() {
        return userId;
    }

    public void setUser(Long user) {
        this.userId = user;
    }

    public Long getRating() {
        /*if(ratingId == null) {
            return new RatingNeo4J(0.0);
        }*/
        return ratingId;
    }

    public void setRating(Long rating) {
        this.ratingId = rating;
    }

    public List<Long> getUpVote() {
        return upVote;
    }

    public void setUpVote(List<Long> upVote) {
        this.upVote = upVote;
    }

    public List<Long> getDownVote() {
        return downVote;
    }

    public void setDownVote(List<Long> downVote) {
        this.downVote = downVote;
    }

    public boolean addUpVote(Long upVote) {

        if( !this.approvalStatus.equals("approved"))
            return false;

        if(!this.upVote.contains(upVote)){
            this.upVote.add(upVote);
            return true;
        }
        return false;
    }

    public boolean addDownVote(Long downVote) {

        if( !this.approvalStatus.equals( "approved") )
            return false;

        if(!this.downVote.contains(downVote)){
            this.downVote.add(downVote);
            return true;
        }
        return false;
    }

    public Review toReview(){
        return new Review(this.getIdReview(), this.getApprovalStatus(), this.getReviewText(), this.getPublishingDate(), this.productId, this.getFunFact(), this.ratingId, this.userId);
    }
}
