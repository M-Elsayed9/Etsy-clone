package com.etsyclone.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Set<Review> findByCustomer_Id(Long customerId);

    Set<Review> findByProduct_Id(Long productId);
}
