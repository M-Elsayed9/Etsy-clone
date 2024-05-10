package com.etsyclone.cart;

import com.etsyclone.cartItem.CartItemDTO;

import com.etsyclone.cartItem.CartItem;
import com.etsyclone.product.Product;
import com.etsyclone.cartItem.CartItemRepository;
import com.etsyclone.product.ProductRepository;
import com.etsyclone.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
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

    @Transactional
    public CartItemDTO addCartItem(Long productId, Long cartId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found with id: " + cartId));

        Optional<CartItem> cartItemOptional = cartItemRepository.findByCart_IdAndProduct_Id(cartId, productId);
        CartItem cartItem;
        if (cartItemOptional.isPresent()) {
            cartItem = cartItemOptional.get();
            cartItem.setQuantity((short) (cartItem.getQuantity() + 1));
        } else {
            cartItem = new CartItem(cart, product, (short) 1);
        }

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        cart.addItem(savedCartItem);
        return convertToDTO(savedCartItem);
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
    public Set<CartItemDTO> getCartItems(Long cartId) {
        Set<CartItem> cartItems = cartItemRepository.findByCart_Id(cartId);
        Set<CartItemDTO> cartItemDTOs = new HashSet<>();
        for (CartItem cartItem : cartItems) {
            cartItemDTOs.add(convertToDTO(cartItem));
        }
        return cartItemDTOs;
    }

    @Transactional
    public CartItemDTO updateCartItem(Long cartItemId, CartItemDTO cartItemDTO) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        short newQuantity = (short) (cartItem.getQuantity() + cartItemDTO.getQuantity());

        if (newQuantity <= 0) {
            cartItemRepository.delete(cartItem);
            cartItem.getCart().removeItem(cartItem);
            return null;
        } else {
            cartItem.setQuantity(newQuantity);
            CartItem savedCartItem = cartItemRepository.save(cartItem);
            cartItem.getCart().updateItem(savedCartItem);
            return convertToDTO(savedCartItem);
        }
    }

    @Transactional
    public void deleteCartItem(Long itemId, Long cartId) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found with id: " + itemId + " in cart: " + cartId));
        cartItemRepository.delete(cartItem);
        cartItem.getCart().removeItem(cartItem);
    }

    @Transactional
    public void deleteAllCartItems(Long cartId) {
        Set<CartItem> cartItems = cartItemRepository.findByCart_Id(cartId);
        cartItemRepository.deleteAll(cartItems);
        cartItems.forEach(cartItem -> cartItem.getCart().removeItem(cartItem));
    }

    private CartItemDTO convertToDTO(CartItem cartItem) {
        return new CartItemDTO(cartItem.getProduct().getId(), cartItem.getQuantity());
    }
}
