package acme.controllers;

import acme.model.CreateReviewDTO;
import acme.model.ReviewDTO;
import acme.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "ReviewCommand", description = "Endpoints for managing Review")
@RequestMapping("api/v1/reviews")
@RestController
class ReviewController {

    @Autowired
    private ReviewService rService;

    @Operation(summary = "creates review")
    @PostMapping("/products/{sku}/reviews")
    public ResponseEntity<ReviewDTO> createReview(@PathVariable(value = "sku") final String sku, @RequestBody CreateReviewDTO createReviewDTO) {
        if (rService.checkIfUserExist(createReviewDTO.getUsername()) != null) {
            rService.createUser(createReviewDTO.getUsername());
        }

        final var review = rService.create(createReviewDTO, sku);

        if(review == null){
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<ReviewDTO>(review, HttpStatus.CREATED);
    }

    @Operation(summary = "deletes review")
    @DeleteMapping("/{reviewID}")
    public ResponseEntity<Boolean> deleteReview(@PathVariable(value = "reviewID") final Long reviewID) {

        Boolean rev = rService.DeleteReview(reviewID);

        if (rev == null) return ResponseEntity.notFound().build();

        if (rev == false) return ResponseEntity.unprocessableEntity().build();

        return ResponseEntity.ok().body(rev);
    }

    @Operation(summary = "Accept or reject review")
    @PutMapping("/acceptreject/{reviewID}")
    public ResponseEntity<ReviewDTO> putAcceptRejectReview(@PathVariable(value = "reviewID") final Long reviewID, @RequestBody String approved){

        try {
            ReviewDTO rev = rService.moderateReview(reviewID, approved);

            return ResponseEntity.ok().body(rev);
        }
        catch( IllegalArgumentException e ) {
            return ResponseEntity.badRequest().build();
        }
        catch( ResourceNotFoundException e ) {
            return ResponseEntity.notFound().build();
        }
    }
}
