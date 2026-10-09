package com.ecommerce.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import com.ecommerce.cart.dto.request.AddToCartRequest;
import com.ecommerce.cart.dto.request.UpdateCartItemRequest;
import com.ecommerce.cart.dto.response.CartResponse;
import com.ecommerce.cart.service.CartService;
import com.ecommerce.exception.ApiResponse;
import com.ecommerce.security.SecurityUtils;


@RestController
@RequestMapping("/api/cart")
@Tag(name = "Cart API", description = "Shopping cart management APIs")
public class CartController 
{

    private final CartService cartService;
    public CartController(CartService cartService) 
    {
        this.cartService = cartService;
    }

    // ==========================================
    // ADD TO CART
    // ==========================================

    @PostMapping("/items")
    @Operation(summary = "Add product to cart")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(Authentication authentication, @Valid @RequestBody AddToCartRequest request) 
    {
        Long userId = SecurityUtils.getCurrentUserId(authentication);

        CartResponse cart = cartService.addToCart(userId, request);

        return ResponseEntity.ok(ApiResponse.success("Product added to cart successfully",cart));
    }

    // ==========================================
    // GET CART
    // ==========================================

    @GetMapping
    @Operation(summary = "Get current user's cart")
    public ResponseEntity<ApiResponse<CartResponse>> getCart(Authentication authentication) 
    {
        Long userId = SecurityUtils.getCurrentUserId(authentication);

        CartResponse cart = cartService.getCart(userId);

        return ResponseEntity.ok(ApiResponse.success("Cart fetched successfully", cart));
    }

    // ==========================================
    // UPDATE CART ITEM
    // ==========================================

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update cart item quantity")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItem(Authentication authentication, @PathVariable Long cartItemId, @Valid @RequestBody UpdateCartItemRequest request) 
    {
        Long userId = SecurityUtils.getCurrentUserId(authentication);

        CartResponse cart = cartService.updateCartItem(userId, cartItemId, request);

        return ResponseEntity.ok(ApiResponse.success("Cart item updated successfully", cart));
    }

    // ==========================================
    // REMOVE CART ITEM
    // ==========================================

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Remove item from cart")
    public ResponseEntity<ApiResponse<CartResponse>> removeCartItem(Authentication authentication, @PathVariable Long cartItemId) 
    {
        Long userId = SecurityUtils.getCurrentUserId(authentication);

        CartResponse cart = cartService.removeCartItem(userId, cartItemId);

        return ResponseEntity.ok(ApiResponse.success("Cart item removed successfully", cart));
    }


    // ==========================================
    // CLEAR CART
    // ==========================================

    @DeleteMapping
    @Operation(summary = "Clear current user's cart")
    public ResponseEntity<ApiResponse<Void>> clearCart(Authentication authentication) 
    {
        Long userId = SecurityUtils.getCurrentUserId(authentication);

        cartService.clearCart(userId);

        return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully", null));
    }
}