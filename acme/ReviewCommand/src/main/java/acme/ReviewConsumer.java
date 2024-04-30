package acme;

import acme.model.Product;
import acme.model.ProductType;
import acme.services.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ReviewConsumer {
    private final ReviewService reviewService;

    @RabbitListener(queues = { "product.queue" })
    public void consumerProduct(ProductType productType) {
        log.info("Consumed {} from queue ", productType);
        Product product = productType.getProduct();
        product.setProductID(500L);
        reviewService.createProduct(product);
    }
}
