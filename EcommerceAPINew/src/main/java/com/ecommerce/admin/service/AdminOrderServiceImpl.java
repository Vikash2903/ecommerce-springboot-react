package com.ecommerce.admin.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.exception.InvalidOrderStatusException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.order.dto.response.OrderItemResponse;
import com.ecommerce.order.dto.response.OrderResponse;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.entity.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderServiceImpl;

@Service
public class AdminOrderServiceImpl implements AdminOrderService
{
	private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	private final OrderRepository orderRepository;
    public AdminOrderServiceImpl(OrderRepository orderRepository) 
    {
		this.orderRepository = orderRepository;
	}

    @Transactional(readOnly = true)
	public Page<OrderResponse> getAllOrders(Pageable pageable) 
	{

	    log.info("Admin fetching all orders");
	    return orderRepository.findAll(pageable).map(this::mapToResponse);
	}
    
    @Transactional(readOnly = true)
    public OrderResponse getAnyOrder(Long orderId) 
    {
        log.info("Admin fetching order {}", orderId);

        Order order = orderRepository.findById(orderId)
              .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: "+ orderId));
        return mapToResponse(order);
    }
    
    
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) 
    {
        log.info("Updating order {} status to {}", orderId, newStatus);

        Order order = orderRepository.findById(orderId)
              .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: "+ orderId));

        OrderStatus currentStatus = order.getStatus();

        if (currentStatus == newStatus) 
        {
            throw new IllegalStateException("Order is already in status "+ newStatus);
        }

        if (!isValidStatusTransition(currentStatus, newStatus)) 
        {
            throw new InvalidOrderStatusException("Invalid order status transition: "+ currentStatus+ " -> "+ newStatus);
        }

        order.setStatus(newStatus);

        Order savedOrder = orderRepository.save(order);

        log.info("Order {} status changed from {} to {}", orderId, currentStatus, newStatus);

        return mapToResponse(savedOrder);
    }
    
    private OrderResponse mapToResponse(Order order) 
    {
        List<OrderItemResponse> items = order.getItems()
                        .stream()
                        .map(this::mapToItemResponse)
                        .toList();

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getShippingAddress(),
                order.getCreatedAt(),
                items
        );
    }
    private OrderItemResponse mapToItemResponse(OrderItem item) 
    {
        return new OrderItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal()
        );
    }
    
    private boolean isValidStatusTransition(OrderStatus current,OrderStatus next) 
    {
        if (current == OrderStatus.PENDING) 
        {
            return next == OrderStatus.CONFIRMED || next == OrderStatus.CANCELLED;
        }

        if (current == OrderStatus.CONFIRMED) 
        {
            return next == OrderStatus.SHIPPED || next == OrderStatus.CANCELLED;
        }

        if (current == OrderStatus.SHIPPED) 
        {
            return next == OrderStatus.DELIVERED;
        }
        return false;
    }
}
