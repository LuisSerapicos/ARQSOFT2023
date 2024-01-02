package com.isep.acme1.Model;

import java.util.List;

public class ProductUserDTO {
    private String sku;
    private String designation;

    private String status;

    private List<String> username;

    public ProductUserDTO(String sku, String designation, String status, List<String> username) {
        this.sku = sku;
        this.designation = designation;
        this.status = status;
        this.username = username;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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
}
