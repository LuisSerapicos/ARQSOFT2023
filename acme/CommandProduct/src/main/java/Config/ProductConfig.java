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

    @Value("${rabbitmq.routing-keys.internal-product}")
    private String internalProductRoutingKey;

    @Bean
    public TopicExchange internalTopicExchange() {
        return new TopicExchange(this.internalExchange);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(this.productQueue);
    }

    @Bean
    public Binding internalToNotificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(internalTopicExchange())
                .with(this.internalProductRoutingKey);
    }
}
