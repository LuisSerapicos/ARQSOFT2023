package com.isep.acme;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.acme.Model.ProductType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
@Slf4j
public class ProductConsumer {

    @RabbitListener(queues = "${rabbitmq.queues.product2}")
    public void consume(Message message) {
        String jsonPayload = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("Consumed {} from queue ", jsonPayload);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ProductType productRequest = objectMapper.readValue(jsonPayload, ProductType.class);
            log.info("Consumed" + productRequest.getProduct());
            log.info("Consumed" + productRequest.getType());


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
