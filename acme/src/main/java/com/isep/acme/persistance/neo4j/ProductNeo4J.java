package com.isep.acme.persistance.neo4j;

import com.isep.acme.model.ProductDTO;
import org.springframework.data.neo4j.core.schema.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.neo4j.ogm.id.UuidStrategy;
import org.neo4j.ogm.typeconversion.UuidStringConverter;
import org.springframework.data.neo4j.core.schema.Node;

import javax.persistence.Convert;
import javax.persistence.GenerationType;
import java.util.Objects;

@Node("ProductNeo4J")
public class ProductNeo4J {

    @Id
    @GeneratedValue
    private Long id;

    public String sku;

    private String designation;

    private String description;
    /*
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> review = new ArrayList<Review>(); */

    protected ProductNeo4J(){}

    public ProductNeo4J(final Long id, final String sku) {
        this.id = Objects.requireNonNull(id);
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
        this.id = id;
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


    public void updateProduct(ProductNeo4J p) {
        setDesignation(p.designation);
        setDescription(p.description);
    }

    public Long getProductID() {
        return id;
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
