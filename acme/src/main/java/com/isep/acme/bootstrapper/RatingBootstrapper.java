package com.isep.acme.bootstrapper;

import com.isep.acme.model.Rating;
import com.isep.acme.repositories.databases.RatingDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RatingBootstrapper implements CommandLineRunner {

    private final RatingDataBase ratingDataBase;

    @Autowired
    public RatingBootstrapper(@Value("${rating.interface.default}") String beanName, ApplicationContext context) {
        this.ratingDataBase = context.getBean(beanName, RatingDataBase.class);
    }

    @Override
    public void run(String... args) throws Exception {

        if(ratingDataBase.findByRate(0.5).isEmpty()) {
            Rating rate05 = new Rating(0.5);
            ratingDataBase.create(rate05);
        }

        if(ratingDataBase.findByRate(1.0).isEmpty()) {
            Rating rate1 = new Rating(1.0);
            ratingDataBase.create(rate1);
        }

        if(ratingDataBase.findByRate(1.5).isEmpty()) {
            Rating rate15 = new Rating(1.5);
            ratingDataBase.create(rate15);
        }

        if(ratingDataBase.findByRate(2.0).isEmpty()) {
            Rating rate2 = new Rating(2.0);
            ratingDataBase.create(rate2);
        }

        if(ratingDataBase.findByRate(2.5).isEmpty()) {
            Rating rate25 = new Rating(2.5);
            ratingDataBase.create(rate25);
        }

        if(ratingDataBase.findByRate(3.0).isEmpty()) {
            Rating rate3 = new Rating(3.0);
            ratingDataBase.create(rate3);
        }

        if(ratingDataBase.findByRate(3.5).isEmpty()) {
            Rating rate35 = new Rating(3.5);
            ratingDataBase.create(rate35);
        }

        if(ratingDataBase.findByRate(4.0).isEmpty()) {
            Rating rate4 = new Rating(4.0);
            ratingDataBase.create(rate4);
        }

        if(ratingDataBase.findByRate(4.5).isEmpty()) {
            Rating rate45 = new Rating(4.5);
            ratingDataBase.create(rate45);
        }

        if(ratingDataBase.findByRate(5.0).isEmpty()) {
            Rating rate5 = new Rating(5.0);
            ratingDataBase.create(rate5);
        }
    }
}
