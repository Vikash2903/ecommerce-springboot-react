package com.ecommerce.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecommerce.order.dto.response.OrderResponse;
import com.ecommerce.order.entity.OrderStatus;

public interface AdminOrderService 
{
	Page<OrderResponse> getAllOrders(Pageable pageable) ;
	OrderResponse getAnyOrder(Long orderId) ;
	OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus);
}
