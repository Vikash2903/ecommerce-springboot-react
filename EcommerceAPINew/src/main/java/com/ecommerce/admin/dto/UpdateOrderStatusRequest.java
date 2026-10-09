package com.ecommerce.admin.dto;

import com.ecommerce.order.entity.OrderStatus;

import jakarta.validation.constraints.NotNull;

public class UpdateOrderStatusRequest 
{

    @NotNull(message = "Order status is required")
    private OrderStatus status;
    
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}