package com.acme.microservices.Model;

import com.isep.acme.model.Product;
import com.isep.acme.model.ProductDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.isep.acme.utils.Generator.generateLongID;

@Document(collection = "product")
@Data
@NoArgsConstructor
public class ProductMongo{

    @Field(value = "productID")
    private Long productID;

    @Indexed(unique = true)
    @Field(value = "sku")
    public String sku;

    @Field(value = "designation")
    private String designation;

    @Field(value = "description")
    private String description;

    public ProductMongo(final String sku) {
        productID = generateLongID();
        setSku(sku);
    }

    public ProductMongo(Long productID, final String sku, final String designation, final String description) {
        this(sku);
        this.productID = productID;
        setDescription(description);
        setDesignation(designation);
    }

    public void setSku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU is a mandatory attribute of Product.");
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

    public Long getProductID() {
        return productID;
    }

    public ProductDTO toDto() {
        return new ProductDTO(this.sku, this.designation);
    }

    public Product toProduct(){
        return new Product(this.productID, this.sku,this.designation,this.description);
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
