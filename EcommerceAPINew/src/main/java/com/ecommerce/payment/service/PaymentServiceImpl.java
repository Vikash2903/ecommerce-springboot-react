package com.ecommerce.payment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.exception.PaymentAlreadyProcessedException;
import com.ecommerce.exception.PaymentProcessingException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.exception.UnauthorizedResourceException;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.payment.dto.PaymentRequest;
import com.ecommerce.payment.dto.PaymentResponse;
import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.entity.PaymentStatus;
import com.ecommerce.payment.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService
{
    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository) 
    {
        this.paymentRepository = paymentRepository;

        this.orderRepository = orderRepository;
    }

    @Transactional
    public PaymentResponse processPayment(Long userId, PaymentRequest request, String idempotencyKey) 
    {
        log.info( "Payment request received. userId={}, orderId={}", userId, request.getOrderId());

        if (idempotencyKey == null || idempotencyKey.isBlank()) 
        {
            throw new PaymentProcessingException("Idempotency-Key header is required");
        }

        /*
         * Check whether this request was already processed.
         */
        var existingPayment = paymentRepository.findByIdempotencyKey(idempotencyKey);

        if (existingPayment.isPresent()) 
        {

            log.info("Duplicate payment request detected. " + "Returning existing payment. key={}", idempotencyKey);

            return mapToResponse(existingPayment.get());
        }

        Order order = orderRepository.findById(request.getOrderId())
               .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: "+ request.getOrderId()));

        /*
         * Verify order ownership.
         */
        if (!order.getUser().getId().equals(userId)) 
        {
            throw new UnauthorizedResourceException("You are not allowed to pay for this order");
        }

        /*
         * Only PENDING orders can be paid.
         */
        if (order.getStatus()!= OrderStatus.PENDING) 
        {
            throw new PaymentProcessingException("Payment is not allowed for order " + order.getId() + " because order status is " + order.getStatus());
        }

        /*
         * Prevent multiple payments for same order.
         */
        var existingOrderPayment = paymentRepository.findByOrderId(order.getId());

        if (existingOrderPayment.isPresent()) 
        {
            Payment payment = existingOrderPayment.get();

            if (payment.getStatus() == PaymentStatus.SUCCESS) 
            {
                throw new PaymentAlreadyProcessedException("Payment has already been completed for order "+ order.getId());
            }
        }

        Payment payment = new Payment();

        payment.setOrder(order);
        
        payment.setAmount(order.getTotalAmount());
        
        payment.setStatus(PaymentStatus.INITIATED);
        
        payment.setIdempotencyKey(idempotencyKey);
        
        payment.setCreatedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        /*
         * Simulated payment gateway.
         *
         * In a real project this is where
         * Razorpay/Stripe/etc. would be called.
         */
        boolean paymentSuccessful = simulatePayment();

        if (!paymentSuccessful) 
        {
            savedPayment.setStatus(PaymentStatus.FAILED);

            savedPayment.setUpdatedAt(LocalDateTime.now());

            paymentRepository.save(savedPayment);

            log.warn("Payment failed. orderId={}", order.getId());

            throw new PaymentProcessingException("Payment failed");
        }

        /*
         * Payment successful.
         */
        savedPayment.setStatus(PaymentStatus.SUCCESS);

        savedPayment.setTransactionId(generateTransactionId());

        savedPayment.setUpdatedAt(LocalDateTime.now());

        paymentRepository.save(savedPayment);

        /*
         * Confirm the order after successful payment.
         */
        order.setStatus(OrderStatus.CONFIRMED);

        orderRepository.save(order);

        log.info("Payment successful. orderId={}, transactionId={}", order.getId(), savedPayment.getTransactionId());

        return mapToResponse(savedPayment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(Long userId, Long paymentId) 
    {
        log.info("Fetching payment {} for user {}", paymentId, userId);

        Payment payment = paymentRepository.findById(paymentId)
                        .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: "+ paymentId));

        if (!payment.getOrder().getUser().getId().equals(userId)) 
        {
            throw new UnauthorizedResourceException("You are not allowed to access this payment");
        }

        return mapToResponse(payment);
    }

    private boolean simulatePayment() 
    {
        /*
         * For now every payment succeeds.
         *
         * Later we can make this configurable
         * so that we can test FAILED payments.
         */
        return true;
    }

    private String generateTransactionId() 
    {

        return "TXN-"
                + UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 16)
                        .toUpperCase();
    }

    private PaymentResponse mapToResponse(Payment payment) 
    {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getCreatedAt()
        );
    }
}