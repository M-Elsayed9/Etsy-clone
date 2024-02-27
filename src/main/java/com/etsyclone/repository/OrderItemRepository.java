package com.etsyclone.repository;

import com.etsyclone.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    void deleteByOrder_Id(Long orderId);

    void deleteByProduct_Id(Long productId);
}
