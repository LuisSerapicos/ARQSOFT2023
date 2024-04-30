package com.isep.acme.persistance.neo4j;

import com.isep.acme.model.AggregatedRating;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("AggregatedRatingNeo4J")
public class AggregatedRatingNeo4J {
    @Id
    @GeneratedValue
    private Long id;

    private Long aggregatedId;

    private double average;

    @Relationship(type = "HAS_PRODUCT", direction = Relationship.Direction.OUTGOING)
    private ProductNeo4J product;

    public AggregatedRatingNeo4J(double average, ProductNeo4J product) {
        this.average = average;
        this.product = product;
    }

    public AggregatedRatingNeo4J(Long aggregatedId, double average, ProductNeo4J product) {
        this.aggregatedId = aggregatedId;
        this.average = average;
        this.product = product;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public ProductNeo4J getProduct() {
        return product;
    }

    public void setProduct(ProductNeo4J product) {
        this.product = product;
    }

    public Long getAggregatedId() {
        return id;
    }

    public void setAggregatedId(Long aggregatedId) {
        this.aggregatedId = aggregatedId;
    }

    public AggregatedRating toAggregatedRating(){
        return new AggregatedRating(this.aggregatedId, this.average,this.product.toProduct());
    }
}
