package com.isep.acme1.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productID;

    @Column(nullable = false, unique = true)
    public String sku;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String description;

    @Column()
    private String status;

    @Column()
    private int numberApprove;

    @ElementCollection
    private List<String> username;

    /*
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> review = new ArrayList<Review>(); */

    public Product(){}

    public Product(final Long productID, final String sku) {
        this.productID = productID;
        setSku(sku);
    }

    public Product(final Long productID, final String sku, final String designation, final String description, final String status, final List<String> username) {
        this.productID= productID;
        this.sku = sku;
        setDescription(description);
        setDesignation(designation);
        this.status = status;
        setUsername(username);
    }

    public Product(final String sku) {
        setSku(sku);
    }

    public Product(final String sku, final String designation, final String description, final String status, final List<String> username) {
        this(sku);
        setDescription(description);
        setDesignation(designation);
        this.status=status;
        this.username = username;
    }
    public Product(final String sku, final String designation, final String description) {
        this(sku);
        setDescription(description);
        setDesignation(designation);
        this.status="pending";
    }

    public Product(final Long productID, final String sku, final String designation, final String description) {
        this.productID= productID;
        this.sku = sku;
        setDescription(description);
        setDesignation(designation);
        this.status="pending";
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


    public void updateProduct(Product p) {
        setDesignation(p.designation);
        setDescription(p.description);
        setStatus(p.getStatus());
        setUsername(p.getUsername());
    }

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public ProductDTO toDto() {
        return new ProductDTO(this.sku, this.designation);
    }

    public ProductUserDTO toUserDto() {
        return new ProductUserDTO(this.sku, this.designation, this.getStatus(), this.getUsername());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumberApprove() {
        return numberApprove;
    }

    public void setNumberApprove(int numberApprove) {
        this.numberApprove = numberApprove;
    }

    public List<String> getUsername() {
        return username;
    }

    public void setUsername(List<String> userID) {
        this.username = userID;
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
