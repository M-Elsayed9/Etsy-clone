package com.etsyclone.repository;

import com.etsyclone.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Set<Review> findByUser_Id(Long userId);

    Set<Review> findByProduct_Id(Long productId);

    Set<Review> findByRating(Integer rating);
}
