package com.ecommerce.exception;

public class PaymentAlreadyProcessedException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;

	public PaymentAlreadyProcessedException(String message) 
    {
        super(message);
    }
}