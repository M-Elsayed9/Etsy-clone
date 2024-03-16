package com.etsyclone.controller.rest;

import com.etsyclone.dto.CartItemDTO;
import com.etsyclone.entity.Cart;
import com.etsyclone.entity.CartItem;
import com.etsyclone.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/carts")
public class CartRestController {

    private final CartService cartService;

    public CartRestController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/{cartId}")
    public ResponseEntity<CartItem> saveOrUpdateCartItem(@PathVariable Long cartId, @RequestBody CartItemDTO cartItemDTO) {
        return ResponseEntity.ok(cartService.addCartItem(cartItemDTO, cartId));
    }

    @GetMapping("/{id}")
    public Cart getCart(@PathVariable Long id) {
        return cartService.getCart(id);
    }

    @GetMapping("/{id}/user")
    public Cart getUserCart(@PathVariable Long id) {
        return cartService.getCartByUserId(id);
    }


   @DeleteMapping("/{cartId}/{itemId}")
    public void deleteCartItem( @PathVariable Long itemId, @PathVariable Long cartId) {
        cartService.deleteCartItem(itemId, cartId);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.deleteAllCartItems(cartId);
        return ResponseEntity.ok().build();
    }
}
