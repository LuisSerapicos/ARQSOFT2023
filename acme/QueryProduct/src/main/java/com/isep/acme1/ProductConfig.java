package com.isep.acme1;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ProductConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queues.product}")
    private String productQueue;

    @Value("${rabbitmq.queues.product2}")
    private String productQueue2;

    @Value("${rabbitmq.routing-keys.internal-product}")
    private String internalReviewRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue productQueue() {
        return new Queue(this.productQueue);
    }

    @Bean
    public Queue productQueue2() {
        return new Queue(this.productQueue2);
    }

    @Bean
    public Binding internalReview() {
        return BindingBuilder
                .bind(productQueue())
                .to(internalTopicExchange())
                .with(this.internalReviewRoutingKey);
    }

    @Bean
    public Binding internalReview2() {
        return BindingBuilder
                .bind(productQueue2())
                .to(internalTopicExchange())
                .with(this.internalReviewRoutingKey);
    }

    public String getInternalExchange() {
        return internalExchange;
    }

    public String getReviewQueue() {
        return productQueue;
    }

    public String getReviewQueue2() {
        return productQueue2;
    }

    public String getInternalReviewRoutingKey() {
        return internalReviewRoutingKey;
    }
}
