package com.etsyclone.repository;

import com.etsyclone.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    void deleteByCart_Id(Long cartId);

    void deleteByProduct_Id(Long productId);

    void deleteByCart_IdAndProduct_Id(Long cartId, Long productId);

    void deleteByCart_IdAndProduct_IdAndQuantity(Long cartId, Long productId, Integer quantity);
}
