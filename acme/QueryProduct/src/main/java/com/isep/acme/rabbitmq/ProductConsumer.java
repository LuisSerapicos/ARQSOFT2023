package com.isep.acme.rabbitmq;

import com.isep.acme.Model.Product;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ProductConsumer {

    @RabbitListener(queues = "product.queue")
    public void consumer(Product productRequest){
        log.info("Consumed {} from queue", productRequest);
    }
}
