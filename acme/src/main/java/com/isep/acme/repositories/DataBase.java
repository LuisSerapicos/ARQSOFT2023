package com.isep.acme.repositories;

import com.isep.acme.model.Product;

import java.util.Optional;

public interface DataBase {

    Optional<Product> findBySku(String sku);
}
