package com.etsyclone.controller.rest;

import com.etsyclone.dto.AddressDTO;
import com.etsyclone.dto.OrderDTO;
import com.etsyclone.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;

    @Autowired
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/users/{userId}")
    public ResponseEntity<OrderDTO> createOrderFromCart(@PathVariable Long userId, @RequestParam Long shippingAddressId) {

            OrderDTO savedOrderDTO = orderService.createOrderFromUserCart(userId, shippingAddressId);
            return new ResponseEntity<>(savedOrderDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        try {
            OrderDTO orderDTO = orderService.getOrderDTO(id);
            return ResponseEntity.ok(orderDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Set<OrderDTO>> getUserOrders(@PathVariable Long userId) {
        try {
            Set<OrderDTO> orders = orderService.getOrdersByUserId(userId);
            return ResponseEntity.ok(orders);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Note: Update and Delete endpoints might not be applicable if you want the orders to be immutable once created.
    // You can remove these if they don't fit your business logic.

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO updatedOrderDTO = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok(updatedOrderDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
