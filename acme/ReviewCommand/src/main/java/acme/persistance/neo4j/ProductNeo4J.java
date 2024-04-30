package acme.persistance.neo4j;

import acme.model.Product;
import acme.model.ProductDTO;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Objects;

@Node("ProductNeo4J")
public class ProductNeo4J {

    @Id
    @GeneratedValue
    private Long id;

    private Long productId;

    public String sku;

    private String designation;

    private String description;
    /*
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> review = new ArrayList<Review>(); */

    protected ProductNeo4J(){}

    public ProductNeo4J(final Long id, final String sku) {
        this.productId = Objects.requireNonNull(id);
        setSku(sku);
    }

    public ProductNeo4J(final Long id, final String sku, final String designation, final String description) {
        this(id, sku);
        setDescription(description);
        setDesignation(designation);
    }

    public ProductNeo4J(final String sku) {
        setSku(sku);
    }

    public ProductNeo4J(final String sku, final String designation, final String description) {
        this(sku);
        setDescription(description);
        setDesignation(designation);
    }

    public void setProductID(Long id) {
        this.productId = id;
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


    public void updateProduct(ProductNeo4J p) {
        setDesignation(p.designation);
        setDescription(p.description);
    }

    public Long getProductID() {
        return productId;
    }

    public ProductDTO toDto() {
        return new ProductDTO(this.sku, this.designation);
    }

    public Product toProduct(){
        return new Product(this.productId, this.sku, this.designation, this.description);
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