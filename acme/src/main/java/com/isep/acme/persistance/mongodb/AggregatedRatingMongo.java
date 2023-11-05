package com.isep.acme.persistance.mongodb;

import com.isep.acme.model.AggregatedRating;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.isep.acme.utils.Generator.generateLongID;

@Document(collection = "aggregatedRating")
@Data
@NoArgsConstructor
public class AggregatedRatingMongo {
    @Field(value = "aggregatedId")
    private Long aggregatedId;

    @Field(value = "average")
    private double average;

    @Field(value = "product")
    private ProductMongo product;

    public AggregatedRatingMongo(double average, ProductMongo product) {
        this.aggregatedId = generateLongID();
        this.average = average;
        this.product = product;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public ProductMongo getProduct() {
        return product;
    }

    public void setProduct(ProductMongo product) {
        this.product = product;
    }

    public Long getAggregatedId() {
        return aggregatedId;
    }

    public AggregatedRating toAggregatedRating(){
        return new AggregatedRating(this.aggregatedId, this.average,this.product.toProduct());
    }
}
