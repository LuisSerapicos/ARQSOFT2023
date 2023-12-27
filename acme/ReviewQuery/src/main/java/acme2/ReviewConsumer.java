package acme2;

import acme2.model.Review;
import acme2.services.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ReviewConsumer {
    private final ReviewService reviewService;

    @RabbitListener(queues = { "${rabbitmq.queue.review}", "${rabbitmq.queue.review2}" })
    public void consumer(Review review) {
        log.info("Consumed {} from queue ", review);
        reviewService.createReview(review);
    }
}
