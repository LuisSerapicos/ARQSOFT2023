package com.isep.acme.persistance.mongodb;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

import static com.isep.acme.utils.Generator.generateLongID;

@Document(collection = "review")
@Data
@NoArgsConstructor
public class ReviewMongo {
    @Field(value = "idReview")
    private Long idReview;

    @Field(value = "version")
    private long version;

    @Field(value = "approvalStatus")
    private String approvalStatus;

    @Field(value = "reviewText")
    private String reviewText;

    @Field(value = "upVote")
    private List<Vote> upVote;

    @Field(value = "downVote")
    private List<Vote> downVote;

    @Field(value = "report")
    private String report;

    @Field(value = "publishingDate")
    private LocalDate publishingDate;

    @Field(value = "funFact")
    private String funFact;

    @Field(value = "product")
    private ProductMongo product;

    @Field(value = "user")
    private UserMongo user;

    @Field(value = "rating")
    private RatingMongo rating;

    public ReviewMongo(long version, String approvalStatus, String reviewText, List<Vote> upVote, List<Vote> downVote, String report, LocalDate publishingDate, String funFact, ProductMongo product, UserMongo user, RatingMongo rating) {
        this.idReview = generateLongID();
        this.version = version;
        this.approvalStatus = approvalStatus;
        this.reviewText = reviewText;
        this.upVote = upVote;
        this.downVote = downVote;
        this.report = report;
        this.publishingDate = LocalDate.now();
        this.funFact = funFact;
        this.product = product;
        this.user = user;
        this.rating = rating;
    }
    public ReviewMongo(long idReview, long version, String approvalStatus, String reviewText, List<Vote> upVote, List<Vote> downVote, String report, LocalDate publishingDate, String funFact, ProductMongo product, UserMongo user, RatingMongo rating) {
        this.idReview = idReview;
        this.version = version;
        this.approvalStatus = approvalStatus;
        this.reviewText = reviewText;
        this.upVote = upVote;
        this.downVote = downVote;
        this.report = report;
        this.publishingDate = LocalDate.now();
        this.funFact = funFact;
        this.product = product;
        this.user = user;
        this.rating = rating;
    }

    public ReviewMongo(long version, String approvalStatus, String reviewText, List<Vote> upVote, List<Vote> downVote, String report, String funFact, ProductMongo product, UserMongo user, RatingMongo rating) {
        this.idReview = generateLongID();
        this.version = version;
        this.approvalStatus = approvalStatus;
        this.reviewText = reviewText;
        this.upVote = upVote;
        this.downVote = downVote;
        this.report = report;
        this.publishingDate = LocalDate.now();
        this.funFact = funFact;
        this.product = product;
        this.user = user;
        this.rating = rating;
    }

    public Long getIdReview() {
        return idReview;
    }

    public List<Vote> getUpVote() {
        return upVote;
    }

    public List<Vote> getDownVote() {
        return downVote;
    }

    public void setUpVote(List<Vote> upVote) {
        this.upVote = upVote;
    }

    public void setDownVote(List<Vote> downVote) {
        this.downVote = downVote;
    }

    public boolean addUpVote(List<Vote> upVote) {
        for(Vote v : upVote){
            if (!this.upVote.contains(v)){
                this.upVote.add(v);
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean addDownVote(Vote downVote) {

        if (!this.approvalStatus.equals("approved"))
            return false;

        if (!this.downVote.contains(downVote)) {
            this.downVote.add(downVote);
            return true;
        }
        return false;
    }

    public Review toReview(){
        return new Review(this.idReview,this.version,this.approvalStatus, this.reviewText,  this.upVote, this.downVote, this.report, this.publishingDate, this.funFact, this.product.toProduct(), this.rating.toRating(), this.user.toUser());
    }

}
