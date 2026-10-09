package com.ecommerce.payment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import com.ecommerce.payment.dto.PaymentRequest;
import com.ecommerce.payment.dto.PaymentResponse;
import com.ecommerce.payment.service.PaymentService;
import com.ecommerce.security.SecurityUtils;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment API", description = "Payment processing APIs")
public class PaymentController 
{
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) 
    {
        this.paymentService = paymentService;
    }

    @PostMapping
    @Operation(summary = "Process payment", description = "Processes a simulated payment for a pending order")
    public ResponseEntity<PaymentResponse> processPayment(
    		
    		Authentication authentication, 
    		
    		@Parameter(description = "Unique key used to prevent duplicate payments", required = true)
    		@RequestHeader("Idempotency-Key") String idempotencyKey, 
            
    		@Valid @RequestBody PaymentRequest request) 
    {

        Long userId = SecurityUtils.getCurrentUserId(authentication);

        PaymentResponse response = paymentService.processPayment(userId, request, idempotencyKey);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get my payment")
    public ResponseEntity<PaymentResponse> getPayment(Authentication authentication, @PathVariable Long paymentId) 
    {
        Long userId = SecurityUtils.getCurrentUserId(authentication);

        return ResponseEntity.ok(paymentService.getPayment(userId, paymentId));
    }
}