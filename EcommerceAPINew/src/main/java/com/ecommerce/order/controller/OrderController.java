package com.ecommerce.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import com.ecommerce.cart.service.CartService;
import com.ecommerce.exception.ApiResponse;
import com.ecommerce.order.dto.request.CreateOrderRequest;
import com.ecommerce.order.dto.response.OrderResponse;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.security.SecurityUtils;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "Order and checkout APIs")
public class OrderController 
{

    private final OrderService orderService;
    
    public OrderController(OrderService orderService, CartService cartService) 
    {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Create order from current cart", description = "Creates an order, validates stock, reduces stock and clears the cart")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(Authentication authentication, @Valid @RequestBody CreateOrderRequest request) 
    {
        Long userId = SecurityUtils.getCurrentUserId(authentication);

        OrderResponse response = orderService.createOrder(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED) .body(
                        ApiResponse.success("Order created successfully", response));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get my order by ID")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(Authentication authentication, @PathVariable Long orderId) 
    {
        Long userId = SecurityUtils.getCurrentUserId(authentication);

        OrderResponse response = orderService.getOrderById(userId, orderId);

        return ResponseEntity.ok(ApiResponse.success("Order fetched successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get my orders")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getMyOrders(Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) 
    {

        Long userId = SecurityUtils.getCurrentUserId(authentication);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<OrderResponse> orders = orderService.getMyOrders(userId, pageable);

        return ResponseEntity.ok(ApiResponse.success("Orders fetched successfully", orders));
    }
    
    @PatchMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel my order", description = "Cancels an order and restores product stock")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(Authentication authentication, @PathVariable Long orderId) 
    {
        Long userId = SecurityUtils.getCurrentUserId(authentication);

        OrderResponse response = orderService.cancelOrder(userId, orderId);

        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully",response));
    }
}