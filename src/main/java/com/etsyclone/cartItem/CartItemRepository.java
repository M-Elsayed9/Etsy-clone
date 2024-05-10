package com.etsyclone.cartItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Set<CartItem> findByCart_Id(Long id);

    Optional<CartItem> findByCart_IdAndProduct_Id(Long cartId, Long productId);
}
