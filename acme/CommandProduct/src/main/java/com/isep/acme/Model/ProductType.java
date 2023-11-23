package com.isep.acme.Model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductType {
    private Product product;
    private String type;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
