package com.etsyclone.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    void deleteByCustomer_Id(Long userId);

    Cart findByCustomer_Id(Long userId);
}
