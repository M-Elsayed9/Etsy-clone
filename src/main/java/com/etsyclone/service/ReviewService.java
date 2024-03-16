package com.etsyclone.service;

import com.etsyclone.entity.Review;
import com.etsyclone.repository.ReviewRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    @Transactional
    public Review saveReview(Review review) {
        Review newReview = reviewRepository.save(review);
        return newReview;
    }

    @Transactional(readOnly = true)
    public Review getReview(Long id) {
        return reviewRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Set<Review> getAllReviews() {
        return Set.copyOf(reviewRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Set<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProduct_Id(productId);
    }

    @Transactional(readOnly = true)
    public Set<Review> getReviewsByCustomerId(Long customerId) {
        return reviewRepository.findByCustomer_Id(customerId);
    }

    @Transactional(readOnly = true)
    public Set<Review> getProductReviews(Long id) {
        return reviewRepository.findByProduct_Id(id);
    }

    @Transactional(readOnly = true)
    public Set<Review> getCustomerReviews(Long id) {
        return reviewRepository.findByCustomer_Id(id);
    }

    @Transactional
    public Review updateReview(Long id, Review review) {
        Review updatedReview = reviewRepository.save(review);
        return updatedReview;
    }

    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
