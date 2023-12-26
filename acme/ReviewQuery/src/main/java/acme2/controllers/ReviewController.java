package acme2.controllers;

import acme2.model.Review;
import acme2.model.ReviewDTO;
import acme2.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "ReviewQuery", description = "Endpoints for managing Review")
@RequestMapping("api/v1/reviews")
@RestController
class ReviewController {

    @Autowired
    private ReviewService rService;

    @GetMapping("/all")
    public ResponseEntity<Iterable<Review>> findAll() {

        final var review = rService.getAll();

        return ResponseEntity.ok().body( review );
    }

    @Operation(summary = "finds a product through its sku and shows its review by status")
    @GetMapping("/products/{sku}/reviews/{status}")
    public ResponseEntity<List<ReviewDTO>> findById(@PathVariable(value = "sku") final String sku, @PathVariable(value = "status") final String status) {

        final var review = rService.getReviewsOfProduct(sku, status);

        return ResponseEntity.ok().body( review );
    }

    @Operation(summary = "gets review by user")
    @GetMapping("/{userID}")
    public ResponseEntity<List<ReviewDTO>> findReviewByUser(@PathVariable(value = "userID") final Long userID) {

        final var review = rService.findReviewsByUser(userID);

        return ResponseEntity.ok().body(review);
    }

    @Operation(summary = "gets pedding reviews")
    @GetMapping("/pending")
    public ResponseEntity<List<ReviewDTO>> getPendingReview(){

        List<ReviewDTO> r = rService.findPendingReview();

        return ResponseEntity.ok().body(r);
    }
}
