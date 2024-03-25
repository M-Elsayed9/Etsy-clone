package com.etsyclone.order;

import com.etsyclone.address.Address;
import com.etsyclone.address.AddressRepository;
import com.etsyclone.cart.Cart;
import com.etsyclone.cart.CartRepository;
import com.etsyclone.cartItem.CartItem;
import com.etsyclone.order.Order;
import com.etsyclone.order.OrderRepository;
import com.etsyclone.orderitem.OrderItem;
import com.etsyclone.orderitem.OrderItemRepository;
import com.etsyclone.product.Product;
import com.etsyclone.product.ProductRepository;
import com.etsyclone.user.User;
import com.etsyclone.user.UserRepository;
import com.etsyclone.order.OrderDTO;
import com.etsyclone.order.OrderDTO.OrderItemDTO;
import com.etsyclone.address.AddressDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserRepository userRepository, CartRepository cartRepository, ProductRepository productRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public OrderDTO createOrderFromUserCart(Long userId, Long addressId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Cart cart = user.getCart();
        Address shippingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("Address not found with id: " + addressId));

        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty or not found for user with id: " + userId);
        }

        if (shippingAddress == null) {
            throw new IllegalArgumentException("Shipping address not found with id: " + addressId);
        }

        Order order = new Order();
        order.setCustomer(user);
        Set<OrderItem> orderItems = new HashSet<>();

        BigDecimal totalOrderPrice = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            totalOrderPrice = totalOrderPrice.add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
            orderItems.add(orderItem);

            Product product = cartItem.getProduct();
            product.setStock((product.getStock() - cartItem.getQuantity()));
            productRepository.save(product);
        }
        order.setOrderItems(orderItems);
        order.setTotalPrice(totalOrderPrice);
        order.setAddress(shippingAddress);

        user.addOrder(order);

        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        cart.clearCart();
        cartRepository.save(cart);

        return convertToDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderDTO(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        return convertToDTO(order);
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
        userRepository.findAll().forEach(user -> user.getOrders().removeIf(order -> order.getId().equals(orderId)));
    }

    @Transactional(readOnly = true)
    public Set<OrderDTO> getOrdersByUserId(Long userId) {
        Set<Order> orders = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId))
                .getOrders();
        return orders.stream().map(this::convertToDTO).collect(Collectors.toSet());
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setUserId(order.getCustomer().getId());
        dto.setOrderItems(order.getOrderItems().stream().map(this::convertToOrderItemDTO).collect(Collectors.toSet()));
        dto.setShippingAddress(new AddressDTO(order.getAddress().getStreet(), order.getAddress().getCity(), order.getAddress().getState(), order.getAddress().getZipCode()));

        return dto;
    }

    private OrderDTO.OrderItemDTO convertToOrderItemDTO(OrderItem orderItem) {
        return new OrderItemDTO(orderItem.getProduct().getId(), orderItem.getQuantity());
    }

    @Transactional
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        Order orderToUpdate = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        orderToUpdate.getOrderItems().clear();

        Set<OrderItem> updatedOrderItems = orderDTO.getOrderItems().stream()
                .map(this::convertToEntity)
                .peek(orderItem -> orderItem.setOrder(orderToUpdate)) // Set the order for each item
                .collect(Collectors.toSet());

        orderToUpdate.setOrderItems(updatedOrderItems);

        orderToUpdate.setTotalPrice(calculateTotalPrice(orderDTO.getOrderItems()));

        Order updatedOrder = orderRepository.save(orderToUpdate);
        return convertToDTO(updatedOrder);
    }

    private Order convertToEntity(OrderDTO dto) {
        User customer = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + dto.getUserId()));

        // Assuming an address service or method is available to fetch the address entity
        Address shippingAddress = addressRepository.findById(dto.getShippingAddress().getId())
                .orElseThrow(() -> new IllegalArgumentException("Address not found with id: " + dto.getShippingAddress().getId()));

        Order order = new Order();
        order.setCustomer(customer);
        order.setAddress(shippingAddress);
        order.setTotalPrice(calculateTotalPrice(dto.getOrderItems())); // Calculate based on the items in the DTO
        // Set other properties from dto to order as needed

        return order;
    }

    private OrderItem convertToEntity(OrderItemDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + dto.getProductId()));

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(product.getPrice()); // Assuming price is directly taken from the product

        return orderItem;
    }

    private BigDecimal calculateTotalPrice(Set<OrderItemDTO> orderItemDTOs) {
        return orderItemDTOs.stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + item.getProductId()));
                    return product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
