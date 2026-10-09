package com.ecommerce.exception;

public class InvalidSortFieldException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;
	
    public InvalidSortFieldException(String message) 
    {
        super(message);
    }
}