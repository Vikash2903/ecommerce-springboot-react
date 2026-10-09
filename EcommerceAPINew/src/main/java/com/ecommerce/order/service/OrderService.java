package com.ecommerce.order.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecommerce.order.dto.request.CreateOrderRequest;
import com.ecommerce.order.dto.response.OrderResponse;


public interface OrderService 
{
	OrderResponse createOrder(Long userId, CreateOrderRequest request);
	
	OrderResponse getOrderById(Long userId, Long orderId);
	
	Page<OrderResponse> getMyOrders(Long userId, Pageable pageable);

	OrderResponse cancelOrder(Long userId, Long orderId); 
}
