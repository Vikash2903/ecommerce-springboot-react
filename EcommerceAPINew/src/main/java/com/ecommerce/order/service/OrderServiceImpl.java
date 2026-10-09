package com.ecommerce.order.service;


import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.entity.CartItem;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.exception.EmptyCartException;
import com.ecommerce.exception.InvalidOrderStatusException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.order.dto.request.CreateOrderRequest;
import com.ecommerce.order.dto.response.OrderItemResponse;
import com.ecommerce.order.dto.response.OrderResponse;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.entity.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService
{
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) 
    {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) 
    {
        log.info("Creating order for user: {}", userId);

        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+ userId));

        Cart cart = cartRepository.findByUserId(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user: "+ userId));

        if (cart.getItems().isEmpty()) 
        {
            throw new EmptyCartException("Cannot create order because cart is empty");
        }

        Order order = new Order();

        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        order.setShippingAddress(request.getShippingAddress().trim());

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) 
        {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            /*
             * Check stock again during checkout.
             *
             * Cart stock may have changed after
             * the item was originally added.
             */
            if (product.getStock() < quantity) 
            {
                throw new InsufficientAuthenticationException("Insufficient stock for product: "+ product.getName()+ ". Available stock: "+ product.getStock());
            }

            BigDecimal unitPrice = product.getPrice();

            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setUnitPrice(unitPrice);
            orderItem.setSubtotal(subtotal);

            order.getItems().add(orderItem);

            total = total.add(subtotal);

            /*
             * Deduct stock.
             */
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
        }

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);

        /*
         * Clear cart after successful order creation.
         */
        cart.getItems().clear();
        cartRepository.save(cart);

        log.info("Order created successfully. orderId={}, userId={}, total={}", savedOrder.getId(), userId, total);
        return mapToResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long userId, Long orderId) 
    {
        log.info("Fetching order {} for user {}", orderId, userId);

        Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: "+ orderId));

        /*
         * Customers can only view their own orders.
         */
        if (!order.getUser().getId().equals(userId)) 
        {
            throw new SecurityException("You are not allowed to access this order");
        }

        return mapToResponse(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getMyOrders(Long userId, Pageable pageable) 
    {
        log.info("Fetching orders for user {}", userId);

        return orderRepository.findByUserId(userId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    public OrderResponse cancelOrder(Long userId, Long orderId) 
    {
        log.info("Customer {} attempting to cancel order {}", userId, orderId);

        Order order = orderRepository.findById(orderId)
           .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: "+ orderId));

        // Customer can cancel only their own order
        if (!order.getUser().getId().equals(userId)) 
        {
            throw new SecurityException("You are not allowed to cancel this order");
        }

        // Check whether cancellation is allowed
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) 
        {
            throw new InvalidOrderStatusException("Order cannot be cancelled when status is "+ order.getStatus());
        }

        // Restore stock
        for (OrderItem item : order.getItems()) 
        {
            Product product = item.getProduct();
            product.setStock(product.getStock()+ item.getQuantity());
            productRepository.save(product);

            log.info("Restored stock for product {} by {} units", product.getId(), item.getQuantity());
        }

        order.setStatus(OrderStatus.CANCELLED);

        Order savedOrder = orderRepository.save(order);

        log.info("Order {} cancelled successfully by user {}", orderId, userId);

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
}