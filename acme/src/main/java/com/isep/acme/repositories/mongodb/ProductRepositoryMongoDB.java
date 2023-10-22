package com.isep.acme.repositories.mongodb;


import com.isep.acme.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepositoryMongoDB extends MongoRepository<Product, String> {
}
