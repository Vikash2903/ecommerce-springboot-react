package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.PaymentRequest;
import com.ecommerce.payment.dto.PaymentResponse;

public interface PaymentService 
{
	PaymentResponse processPayment(Long userId, PaymentRequest request, String idempotencyKey);
	
	PaymentResponse getPayment(Long userId, Long paymentId);
}
