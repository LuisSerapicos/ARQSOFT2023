package com.isep.acme1.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.acme1.Model.Product;
import com.isep.acme1.Model.ProductType;
import com.isep.acme1.Service.ProductService;
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
    private final ProductService productService;
    @RabbitListener(queues = "${rabbitmq.queues.product}")
    public void consume(Message message) {
        String jsonPayload = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("Consumed {} from queue ", jsonPayload);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ProductType productRequest = objectMapper.readValue(jsonPayload, ProductType.class);
            log.info("Consumed" + productRequest.getProduct());
            log.info("Consumed" + productRequest.getType());
            switch (productRequest.getType()){
                case "add":
                    create(productRequest.getProduct());
                case "update":
                    update(productRequest.getProduct());
                case "delete":
                    delete(productRequest.getProduct());
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void delete(Product product) {
        productService.deleteBySku(product.getSku());
    }

    private void update(Product product) {
        productService.updateBySku(product.getSku(), product);
    }

    public void create(Product product){
        productService.createProduct(product);
    }


}
