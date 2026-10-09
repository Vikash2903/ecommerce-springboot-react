package com.ecommerce.exception;

public class UnauthorizedResourceException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;
	
    public UnauthorizedResourceException(String message) 
    {
        super(message);
    }
}