package com.isep.acme.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;


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

    @Field(value = "status")
    private String status;

    @Field(value = "username")
    private List<String> username;

    @Field(value = "numberApprove")
    private int numberApprove;

    public ProductMongo(final String sku) {
        productID = Generator.generateLongID();
        setSku(sku);
    }

    public ProductMongo(Long productID, final String sku, final String designation, final String description, final String status) {
        this(sku);
        this.productID = productID;
        setDescription(description);
        setDesignation(designation);
        this.status= status;
    }

    public ProductMongo(Long productID, final String sku, final String designation, final String description, final String status, String username) {
        this(sku);
        this.productID = productID;
        setDescription(description);
        setDesignation(designation);
        this.status = status;
        addUsername(username);
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
        return new Product(this.productID, this.sku,this.designation,this.description,this.status,this.getUsername());
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getUsername() {
        return username;
    }

    public void setUsername(List<String> username) {
        this.username = username;
    }

    public void addUsername(String username){
        if (this.username == null) {
            this.username = new ArrayList<>();
        }
        this.username.add(username);
    }

    public int getNumberApprove() {
        return numberApprove;
    }

    public void setNumberApprove(int numberApprove) {
        this.numberApprove = numberApprove;
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
