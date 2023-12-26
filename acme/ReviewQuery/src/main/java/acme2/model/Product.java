package acme2.model;

import javax.persistence.*;

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
    /*
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> review = new ArrayList<Review>(); */

    public Product(){}

    public Product(final Long productID, final String sku) {
        this.productID = productID;
        setSku(sku);
    }

    public Product(final Long productID, final String sku, final String designation, final String description) {
        this.productID= productID;
        this.sku = sku;
        setDescription(description);
        setDesignation(designation);
    }

    public Product(final String sku) {
        setSku(sku);
    }

    public Product(final String sku, final String designation, final String description) {
        this(sku);
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


    public void updateProduct(Product p) {
        setDesignation(p.designation);
        setDescription(p.description);
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
/*
    public List<Review> getReview() {
        return review;
    }

    public void setReview(List<Review> review) {
        this.review = review;
    }
*/

}
