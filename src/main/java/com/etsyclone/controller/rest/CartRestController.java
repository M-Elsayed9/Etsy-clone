package com.etsyclone.controller.rest;

import com.etsyclone.dto.CartItemDTO;
import com.etsyclone.entity.Cart;
import com.etsyclone.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/carts")
public class CartRestController {

    private final CartService cartService;

    @Autowired
    public CartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{cartId}")
    public ResponseEntity<CartItemDTO> addCartItem(@PathVariable Long cartId, @RequestBody CartItemDTO cartItemDTO) {
        CartItemDTO savedCartItemDTO = cartService.addCartItem(cartItemDTO, cartId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCartItemDTO);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.getCart(cartId));
    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<Set<CartItemDTO>> getCartItems(@PathVariable Long cartId) {
        Set<CartItemDTO> cartItemDTOs = cartService.getCartItems(cartId);
        return ResponseEntity.ok(cartItemDTOs);
    }

    @PutMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartItemDTO> updateCartItem(@PathVariable Long itemId, @RequestBody CartItemDTO cartItemDTO) {
        CartItemDTO updatedCartItemDTO = cartService.updateCartItem(itemId, cartItemDTO);
        if (updatedCartItemDTO == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(updatedCartItemDTO);
        }
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartId, @PathVariable Long itemId) {
        cartService.deleteCartItem(itemId, cartId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.deleteAllCartItems(cartId);
        return ResponseEntity.ok().build();
    }
}
