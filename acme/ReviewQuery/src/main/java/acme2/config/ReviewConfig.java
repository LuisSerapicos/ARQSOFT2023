package acme2.config;

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
public class ReviewConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queue.review}")
    private String reviewQueue;

    @Value("${rabbitmq.queue.review2}")
    private String reviewQueue2;

    @Value("${rabbitmq.routing-keys.internal-review}")
    private String internalReviewRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue reviewQueue() {
        return new Queue(this.reviewQueue);
    }

    @Bean
    public Queue reviewQueue2() {
        return new Queue(this.reviewQueue2);
    }

    @Bean
    public Binding internalReview() {
        return BindingBuilder
                .bind(reviewQueue())
                .to(internalTopicExchange())
                .with(this.internalReviewRoutingKey);
    }

    @Bean
    public Binding internalReview2() {
        return BindingBuilder
                .bind(reviewQueue2())
                .to(internalTopicExchange())
                .with(this.internalReviewRoutingKey);
    }

    public String getInternalExchange() {
        return internalExchange;
    }

    public String getReviewQueue() {
        return reviewQueue;
    }

    public String getReviewQueue2() {
        return reviewQueue2;
    }

    public String getInternalReviewRoutingKey() {
        return internalReviewRoutingKey;
    }
}
