package com.isep.acme.persistance.mongodb;

import com.isep.acme.model.Product;
import com.isep.acme.model.ProductDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


import java.util.Objects;

@Document(collection = "product")
@Data
@NoArgsConstructor
public class ProductMongo{


    private ObjectId productID;


    @Indexed(unique = true)
    @Field(value = "sku")
    public String sku;

    @Field(value = "designation")
    private String designation;

    @Field(value = "description")
    private String description;


    public ProductMongo(final ObjectId productID, final String sku, final String designation, final String description) {
        this(productID, sku);
        setDescription(description);
        setDesignation(designation);
    }

    public ProductMongo(final String sku) {
        setSku(sku);
    }

    public ProductMongo(final String sku, final String designation, final String description) {
        this(sku);
        setDescription(description);
        setDesignation(designation);
    }

    public ProductMongo(final ObjectId productID, final String sku) {
        this.productID = Objects.requireNonNull(productID);
        setSku(sku);
    }

    public void setSku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU is a mandatory attribute of Product.");
        }
        if (sku.length() != 12) {
            throw new IllegalArgumentException("SKU must be 12 characters long.");
        }

        this.sku = sku;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        if (designation == null || designation.isBlank()) {
            throw new IllegalArgumentException("Designation is a mandatory attribute of Product.");
        }
        if (designation.length() > 50) {
            throw new IllegalArgumentException("Designation must not be greater than 50 characters.");
        }
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is a mandatory attribute of Product.");
        }

        if (description.length() > 1200) {
            throw new IllegalArgumentException("Description must not be greater than 1200 characters.");
        }

        this.description = description;
    }

    public String getSku() {
        return sku;
    }


    public void updateProduct(ProductMongo p) {
        setDesignation(p.designation);
        setDescription(p.description);
    }

    public ObjectId getProductID() {
        return productID;
    }

    public ProductDTO toDto() {
        return new ProductDTO(this.sku, this.designation);
    }

    public Product toProduct(){
        return new Product(this.sku,this.designation,this.description);
    }


/*
    public List<Review> getReview() {
        return review;
    }

    public void setReview(List<Review> review) {
        this.review = review;
    }
*/


}
