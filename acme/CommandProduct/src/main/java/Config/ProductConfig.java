package Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.TopicExchange;

@Configuration
public class ProductConfig {
    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queues.product}")
    private String productQueue;

    @Value("${rabbitmq.queues.product2}")
    private String product2Queue;

    @Value("${rabbitmq.routing-keys.internal-product}")
    private String internalProductRoutingKey;

    @Bean
    public TopicExchange internalExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue productQueue() {
        return new Queue(this.productQueue);
    }

    @Bean
    public Queue productQueue2() {
        return new Queue(this.product2Queue);
    }

    @Bean
    public Binding internalProduct() {
        return BindingBuilder
                .bind(productQueue())
                .to(internalExchange())
                .with(this.internalProductRoutingKey);
    }

    @Bean
    public Binding internalProduct2() {
        return BindingBuilder
                .bind(productQueue2())
                .to(internalExchange())
                .with(this.internalProductRoutingKey);
    }
}
