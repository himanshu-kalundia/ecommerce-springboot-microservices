package com.pcshop.cartservice.controller;

import com.pcshop.cartservice.dto.AddToCartRequest;
import com.pcshop.cartservice.dto.CartItemResponse;
import com.pcshop.cartservice.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    // GET /api/cart - returns enriched product info + total prices
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(service.getCartItems(username));
    }

    // POST /api/cart - adds product to cart with enrichment
    @PostMapping
    public ResponseEntity<CartItemResponse> addToCart(@RequestBody AddToCartRequest request,
                                                      Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(service.addToCart(username, request));
    }

    // DELETE /api/cart/{itemId} - removes one cart item
    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable Long itemId) {
        service.removeItem(itemId);
        return ResponseEntity.ok("Item removed");
    }

    // DELETE /api/cart - clears entire cart for user
    @DeleteMapping
    public ResponseEntity<?> clearCart(Authentication authentication) {
        String username = authentication.getName();
        service.clearCart(username);
        return ResponseEntity.ok("Cart cleared");
    }
}
