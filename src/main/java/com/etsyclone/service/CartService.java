package com.etsyclone.service;

import com.etsyclone.dto.CartItemDTO;

import com.etsyclone.entity.Cart;
import com.etsyclone.entity.CartItem;
import com.etsyclone.entity.Product;
import com.etsyclone.repository.CartItemRepository;
import com.etsyclone.repository.CartRepository;
import com.etsyclone.repository.ProductRepository;
import com.etsyclone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Cart addCart(Cart cart) {
        return cartRepository.save(cart);
    }
    @Transactional(readOnly = true)
    public Cart getCart(Long id) {
        return cartRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByCustomer_Id(userId);
    }

    @Transactional(readOnly = true)
    public Set<CartItem> getCartItems(Long id) {
        return cartItemRepository.findByCart_Id(id);
    }

    @Transactional
    public CartItem addCartItem(CartItemDTO cartItemDTO, Long cartId) {
        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst();

        CartItem cartItem;
        if (existingItem.isPresent()) {
            cartItem = existingItem.get();
            short newQuantity = (short) (cartItem.getQuantity() + cartItemDTO.getQuantity());

            newQuantity = (newQuantity <= 0) ? 0 : newQuantity;

            cartItem.setQuantity(newQuantity);
        } else {
            short initialQuantity = (cartItemDTO.getQuantity() < 0) ? 0 : cartItemDTO.getQuantity();

            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(initialQuantity);
            cart.setTotalPrice(cart.getTotalPrice());
        }

        cart.updateItem(cartItem);
        userRepository.save(cart.getCustomer());
        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void deleteCartItem(Long cartItemId, Long cartId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));

        Product product = cartItem.getProduct();
        BigDecimal priceReduction = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
        BigDecimal newTotalPrice = cart.getTotalPrice().subtract(priceReduction);

        if (newTotalPrice.compareTo(BigDecimal.ZERO) < 0) {
            newTotalPrice = BigDecimal.ZERO;
        }

        cart.setTotalPrice(newTotalPrice);
        cart.getItems().remove(cartItem);
        cartRepository.save(cart);
        cartItemRepository.deleteById(cartItemId);
    }

    @Transactional
    public void deleteAllCartItems(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + cartId));

        cart.clearCart();

        cart.setTotalPrice(BigDecimal.ZERO);

        // Save the updated cart to persist the changes
        cartRepository.save(cart);
    }


}
