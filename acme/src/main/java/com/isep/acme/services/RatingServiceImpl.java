package com.isep.acme.services;

import com.isep.acme.model.Rating;
import com.isep.acme.repositories.RatingRepository;
import com.isep.acme.repositories.databases.RatingDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.isep.acme.model.Rating;
import com.isep.acme.repositories.databases.RatingDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingServiceImpl implements RatingService{

    private final RatingDataBase ratingDataBase;

    @Autowired
    public RatingServiceImpl(@Value("${rating.interface.default}") String beanName , ApplicationContext context) {
        this.ratingDataBase = context.getBean(beanName, RatingDataBase.class);
    }

    public Optional<Rating> findByRate(Double rate){
        return ratingDataBase.findByRate(rate);
    }
}
