package com.isep.acme1.rabbitmq;

import com.isep.acme1.Model.Product;
import com.isep.acme1.Model.ProductMongo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ProductConsumer {

    @RabbitListener(queues =  {"${rabbitmq.queues.product}", })
    public void consumer(Product productRequest){
        log.info("Consumed {} from queue", productRequest);
    }
}
