package acme2.persistance.neo4j;

import acme2.model.Review;
import acme2.model.Vote;
import acme2.persistance.neo4j.ProductNeo4J;
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
    private List<Vote> upVote;

    @Relationship(type = "DOWNVOTED_BY", direction = Relationship.Direction.INCOMING)
    private List<Vote> downVote;

    private String report;

    private LocalDate publishingDate;

    private String funFact;

    @Relationship(type = "FOR_PRODUCT", direction = Relationship.Direction.OUTGOING)
    private ProductNeo4J product;

    @Relationship(type = "BY_USER", direction = Relationship.Direction.OUTGOING)
    private UserNeo4J user;

    @Relationship(type = "HAS_RATING", direction = Relationship.Direction.OUTGOING)
    private RatingNeo4J rating;

    protected ReviewNeo4J(){}

    public ReviewNeo4J(final Long idReview, final long version, final String approvalStatus, final String reviewText, final LocalDate publishingDate, final String funFact) {
        this.idReview = Objects.requireNonNull(idReview);
        this.version = Objects.requireNonNull(version);
        setApprovalStatus(approvalStatus);
        setReviewText(reviewText);
        setPublishingDate(publishingDate);
        setFunFact(funFact);
    }

    public ReviewNeo4J(final Long idReview, final long version, final String approvalStatus, final  String reviewText, final List<Vote> upVote, final List<Vote> downVote, final String report, final LocalDate publishingDate, final String funFact, ProductNeo4J product, RatingNeo4J rating, UserNeo4J user) {
        this(idReview, version, approvalStatus, reviewText, publishingDate, funFact);

        setUpVote(upVote);
        setDownVote(downVote);
        setReport(report);
        setProduct(product);
        setRating(rating);
        setUser(user);

    }

    public ReviewNeo4J(final String reviewText, LocalDate publishingDate, ProductNeo4J product, String funFact, RatingNeo4J rating, UserNeo4J user) {
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

    public void setProduct(ProductNeo4J product) {
        this.product = product;
    }

    public ProductNeo4J getProduct() {
        return product;
    }

    public UserNeo4J getUser() {
        return user;
    }

    public void setUser(UserNeo4J user) {
        this.user = user;
    }

    public RatingNeo4J getRating() {
        if(rating == null) {
            return new RatingNeo4J(0.0);
        }
        return rating;
    }

    public void setRating(RatingNeo4J rating) {
        this.rating = rating;
    }

    public List<Vote> getUpVote() {
        return upVote;
    }

    public void setUpVote(List<Vote> upVote) {
        this.upVote = upVote;
    }

    public List<Vote> getDownVote() {
        return downVote;
    }

    public void setDownVote(List<Vote> downVote) {
        this.downVote = downVote;
    }

    public boolean addUpVote(Vote upVote) {

        if( !this.approvalStatus.equals("approved"))
            return false;

        if(!this.upVote.contains(upVote)){
            this.upVote.add(upVote);
            return true;
        }
        return false;
    }

    public boolean addDownVote(Vote downVote) {

        if( !this.approvalStatus.equals( "approved") )
            return false;

        if(!this.downVote.contains(downVote)){
            this.downVote.add(downVote);
            return true;
        }
        return false;
    }

    public Review toReview(){
        return new Review(this.getIdReview(), this.getApprovalStatus(), this.getReviewText(), this.getPublishingDate(), this.product.toProduct(), this.getFunFact(), this.rating.toRating(), this.user.toUser());
    }
}
