package com.isep.acme.services;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.model.Product;
import com.isep.acme.repositories.databases.AggregatedRatingDataBase;
import com.isep.acme.repositories.databases.ProductDataBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AggregatedRatingServiceImpl implements AggregatedRatingService {

    private final AggregatedRatingDataBase aggregatedRatingDataBase;
    private final ProductDataBase productDataBase;

    @Autowired
    ReviewService rService;

    @Autowired
    public AggregatedRatingServiceImpl(@Value("${database.interface.default}") String beanName2, @Value("${aggregated.interface.default}") String beanName3, ApplicationContext context) {
        this.productDataBase = context.getBean(beanName2, ProductDataBase.class);
        this.aggregatedRatingDataBase = context.getBean(beanName3, AggregatedRatingDataBase.class);
    }

    @Override
    public AggregatedRating save(String sku) {
        Optional<Product> product = productDataBase.findBySku(sku);
        if (product.isEmpty()) {
            return null;
        }

        Double average = rService.getWeightedAverage(product.get());


        Optional<AggregatedRating> r = aggregatedRatingDataBase.findByProductId(product.get());
        AggregatedRating aggregateF;

        if (r.isPresent()) {
            r.get().setAverage(average);
            aggregateF = aggregatedRatingDataBase.create(r.get());
        } else {
            AggregatedRating f = new AggregatedRating(average, product.get());
            aggregateF = aggregatedRatingDataBase.create(f);
        }

        return aggregateF;
    }


}
