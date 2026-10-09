package com.ecommerce.order.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ecommerce.order.entity.OrderStatus;

public class OrderResponse 
{
    private Long id;
    
    private Long userId;
    
    private OrderStatus status;
    
    private BigDecimal totalAmount;
    
    private String shippingAddress;
    
    private LocalDateTime createdAt;
    
    private List<OrderItemResponse> items;

    public OrderResponse(Long id, Long userId, OrderStatus status, BigDecimal totalAmount, String shippingAddress, LocalDateTime createdAt, List<OrderItemResponse> items) 
    {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }
}