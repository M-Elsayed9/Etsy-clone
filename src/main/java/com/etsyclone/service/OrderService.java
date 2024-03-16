package com.etsyclone.service;

import com.etsyclone.entity.Order;
import com.etsyclone.entity.OrderItem;
import com.etsyclone.entity.User;
import com.etsyclone.repository.OrderItemRepository;
import com.etsyclone.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
    }

    @Transactional
    public Order addOrder(Order order, Long userId) {
        User customer = userService.getUser(userId);
        Order newOrder = new Order(customer, order.getAddress(), order.getTotalPrice());
        customer.getOrders().add(newOrder);
        return orderRepository.save(newOrder);
    }

    @Transactional
    public ResponseEntity<OrderItem> addOrderItem(OrderItem orderItem, Long orderId) {
        Order order = orderRepository.findById(orderId).get();
        orderItem.setOrder(order);
        return ResponseEntity.ok(orderItemRepository.save(orderItem));
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long id) {
        return orderRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Set<Order> getAllOrders() {
        return Set.copyOf(orderRepository.findAll());
    }

    public Set<Order> getOrderItems(Long id) {
        return orderItemRepository.findByOrder_Id(id);
    }

    @Transactional(readOnly = true)
    public Set<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomer_Id(customerId);
    }

    @Transactional
    public Order updateOrder(Order order, Long userId) {
        User customer = userService.getUser(userId);
        order.setCustomer(customer);
        customer.getOrders().add(order);

        return orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

}
