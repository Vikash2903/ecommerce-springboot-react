package com.ecommerce.order.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> 
{
    Page<Order> findByUserId(Long userId, Pageable pageable);
    
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}