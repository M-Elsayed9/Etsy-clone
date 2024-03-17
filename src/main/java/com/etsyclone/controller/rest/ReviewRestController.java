package com.etsyclone.controller.rest;

import com.etsyclone.entity.Review;
import com.etsyclone.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/reviews")
public class ReviewRestController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review addReview(Review review) {
        return reviewService.saveReview(review);
    }

    @GetMapping
    public Set<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public Review getReviewById(Long id) {
        return reviewService.getReview(id);
    }

    @GetMapping("/{id}/user")
    public Set<Review> getCustomerReviews(Long id) {
        return reviewService.getCustomerReviews(id);
    }

    @GetMapping("/{id}/product")
    public Set<Review> getReviewsByProductId(Long productId) {
        return reviewService.getReviewsByProductId(productId);
    }

    @PutMapping("/{id}")
    public Review updateReview(Long id, Review review) {
        return reviewService.updateReview(id, review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(Long id) {
        reviewService.deleteReview(id);
    }

}
