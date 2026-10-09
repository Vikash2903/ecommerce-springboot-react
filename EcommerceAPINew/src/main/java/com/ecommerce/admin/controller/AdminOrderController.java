package com.ecommerce.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.ecommerce.admin.dto.UpdateOrderStatusRequest;
import com.ecommerce.admin.service.AdminOrderService;
import com.ecommerce.order.dto.response.OrderResponse;

@RestController
@RequestMapping("/api/admin/orders")
@Tag(name = "Admin Order API",description = "Admin order management APIs"
)
public class AdminOrderController 
{
    private final AdminOrderService orderService;

    public AdminOrderController(AdminOrderService orderService) 
    {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "Get all orders")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
    		@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int size) 
    {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(orderService.getAllOrders(pageable));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get any order by ID")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) 
    {
        return ResponseEntity.ok(orderService.getAnyOrder(orderId));
    }

    @PatchMapping("/{orderId}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long orderId, @Valid @RequestBody UpdateOrderStatusRequest request) 
    {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, request.getStatus()));
    }
}