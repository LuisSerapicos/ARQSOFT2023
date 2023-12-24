package com.isep.acme.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idReview;

    @Version
    private long version;

    @Column(nullable = false)
    private String approvalStatus;

    @Column(nullable = false)
    private String reviewText;

    @ElementCollection
    @Column(nullable = true)
    private List<Long> userUpVoteId;

    @ElementCollection
    @Column(nullable = true)
    private List<Long> userDownVoteId;

    @Column(nullable = true)
    private String report;

    @Column(nullable = false)
    private LocalDate publishingDate;

    @Column(nullable = false)
    private String funFact;

    private Long productId;

    private Long userId;

    private Long ratingId;

    protected Review() {
    }

    public Review(long version, String approvalStatus, String reviewText, List<Long> upVote, List<Long> downVote, String report, LocalDate publishingDate, String funFact, Long product, Long user, Long rating) {
        this.version = version;
        this.approvalStatus = approvalStatus;
        this.reviewText = reviewText;
        this.userUpVoteId = upVote;
        this.userDownVoteId = downVote;
        this.report = report;
        this.publishingDate = publishingDate;
        this.funFact = funFact;
        this.productId = product;
        this.userId = user;
        this.ratingId = rating;
    }

    public Review(final Long idReview, final long version, final String approvalStatus, final String reviewText, final LocalDate publishingDate, final String funFact) {
        this.idReview = Objects.requireNonNull(idReview);
        this.version = Objects.requireNonNull(version);
        setApprovalStatus(approvalStatus);
        setReviewText(reviewText);
        setPublishingDate(publishingDate);
        setFunFact(funFact);
    }

    public Review(final Long idReview, final long version, final String approvalStatus, final String reviewText, final List<Long> upVote, final List<Long> downVote, final String report, final LocalDate publishingDate, final String funFact, Long product, Long rating, Long user) {
        this(idReview, version, approvalStatus, reviewText, publishingDate, funFact);

        setUpVote(upVote);
        setDownVote(downVote);
        setProduct(product);
        setRating(rating);
        setUser(user);

    }

    public Review(final Long idReview, final String reviewText, LocalDate publishingDate, Long product, String funFact, Long rating, Long user) {
        setIdReview(idReview);
        setReviewText(reviewText);
        setProduct(product);
        setPublishingDate(publishingDate);
        setApprovalStatus("pending");
        setFunFact(funFact);
        setRating(rating);
        setUser(user);
        this.userUpVoteId = new ArrayList<>();
        this.userDownVoteId = new ArrayList<>();
    }

    public Review(final Long idReview, final String approvalStatus, final String reviewText, LocalDate publishingDate, Long product, String funFact, Long rating, Long user) {
        setIdReview(idReview);
        setReviewText(reviewText);
        setProduct(product);
        setPublishingDate(publishingDate);
        setApprovalStatus(approvalStatus);
        setFunFact(funFact);
        setRating(rating);
        setUser(user);
        this.userUpVoteId = new ArrayList<>();
        this.userDownVoteId = new ArrayList<>();
    }

    public Review(Long idReview, final String reviewText, LocalDate publishingDate, String approvalStatus, Long product, String funFact, Long rating, Long user) {
        this.idReview = idReview;
        setReviewText(reviewText);
        setProduct(product);
        setPublishingDate(publishingDate);
        setApprovalStatus(approvalStatus);
        setFunFact(funFact);
        setRating(rating);
        setUser(user);
        this.userUpVoteId = new ArrayList<>();
        this.userDownVoteId = new ArrayList<>();
    }

    public Long getIdReview() {
        return idReview;
    }

    public void setIdReview(Long idReview) {
        this.idReview = idReview;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public Boolean setApprovalStatus(String approvalStatus) {

        if (approvalStatus.equalsIgnoreCase("pending") ||
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

    public String getReport() {
        return report;
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
        /*if (ratingId == null) {
            return new Long(0.0);
        }*/
        return ratingId;
    }

    public void setRating(Long rating) {
        this.ratingId = rating;
    }

    public List<Long> getUpVote() {
        return userUpVoteId;
    }

    public void setUpVote(List<Long> upVote) {
        this.userUpVoteId = upVote;
    }

    public List<Long> getDownVote() {
        return userDownVoteId;
    }

    public void setDownVote(List<Long> downVote) {
        this.userDownVoteId = downVote;
    }

    public boolean addUpVote(Long upVote) {

        if (!this.approvalStatus.equals("approved"))
            return false;

        if (!this.userUpVoteId.contains(upVote)) {
            this.userUpVoteId.add(upVote);
            return true;
        }
        return false;
    }

    public boolean addDownVote(Long downVote) {

        if (!this.approvalStatus.equals("approved"))
            return false;

        if (!this.userDownVoteId.contains(downVote)) {
            this.userDownVoteId.add(downVote);
            return true;
        }
        return false;
    }

    public Review toReview() {
        return new Review(this.idReview, this.reviewText, this.publishingDate, this.productId, this.funFact, this.ratingId, this.userId);
    }
}
