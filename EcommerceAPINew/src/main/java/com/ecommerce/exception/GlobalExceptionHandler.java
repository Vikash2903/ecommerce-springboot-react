 package com.ecommerce.exception;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler 
{
    /*
     * ==============================
     * 404 - RESOURCE NOT FOUND
     * ==============================
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) 
    {
    	ApiErrorResponse response = buildResponse(false, ex.getMessage());
    	
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /*
     * ==============================
     * 409 - DUPLICATE RESOURCE
     * ==============================
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateResource(DuplicateResourceException ex, HttpServletRequest request) 
    {
    	ApiErrorResponse response = buildResponse(false, ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(
            BadCredentialsException ex) {

    	 System.out.println(
    	            ">>> BadCredentialsException handler called"
    	    );
        ApiErrorResponse response =
                new ApiErrorResponse(
                        false,
                        "Invalid email or password",
                        LocalDateTime.now()
                );
        System.out.println(
                ">>> Returning: " + response.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }
    
    /*
     * ==============================
     * 400 - INVALID SORT FIELD
     * ==============================
     */
    @ExceptionHandler(InvalidSortFieldException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidSortField(InvalidSortFieldException ex, HttpServletRequest request) 
    {
    	ApiErrorResponse response = buildResponse(false, ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /*
     * ==============================
     * 400 - INSUFFICIENT STOCK
     * ==============================
     */
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiErrorResponse> handleInsufficientStock(InsufficientStockException ex, HttpServletRequest request) 
    {
    	ApiErrorResponse response = buildResponse(false, ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /*
     * ==============================
     * 400 - EMPTY CART
     * ==============================
     */
    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<ApiErrorResponse> handleEmptyCart(EmptyCartException ex, HttpServletRequest request) 
    {
    	ApiErrorResponse response = buildResponse(false, ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /*
     * ==============================
     * 400 - INVALID ORDER STATUS
     * ==============================
     */
    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidOrderStatus(InvalidOrderStatusException ex, HttpServletRequest request) 
    {
    	ApiErrorResponse response = buildResponse(false, ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /*
     * ==============================
     * 403 - RESOURCE ACCESS DENIED
     * ==============================
     */
    @ExceptionHandler(UnauthorizedResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorizedResource(UnauthorizedResourceException ex, HttpServletRequest request) 
    {
    	ApiErrorResponse response = buildResponse(false, ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /*
     * ==============================
     * 400 - INVALID REQUEST
     * ==============================
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRequest(InvalidRequestException ex, HttpServletRequest request) 
    {
    	ApiErrorResponse response = buildResponse(false, ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /*
     * ==============================
     * 400 - DTO VALIDATION
     * ==============================
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) 
    {
        Map<String, String> errors =
                new LinkedHashMap<>();

        for (FieldError fieldError :ex.getBindingResult().getFieldErrors()) 
        {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiErrorResponse response = buildResponse(false,  "Validation failed");
        response.setValidationErrors(errors);

        return ResponseEntity.badRequest().body(response);
    }

    /*
     * ==============================
     * 400 - ILLEGAL STATE
     * ==============================
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalState(IllegalStateException ex,HttpServletRequest request) 
    {
    	ApiErrorResponse response = buildResponse(false, ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /*
     * ==============================
     * 400 - ILLEGAL ARGUMENT
     * ==============================
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) 
    {
    	ApiErrorResponse response = buildResponse(false, ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /*
     * ==============================
     * 500 - UNEXPECTED ERROR
     * ==============================
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) 
    {
        /*
         * Don't expose internal exception details
         * to the client.
         */
    	ApiErrorResponse response = buildResponse(false, "An unexpected error occurred");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    @ExceptionHandler(PaymentAlreadyProcessedException.class)
    public ResponseEntity<ApiErrorResponse>handlePaymentAlreadyProcessed(PaymentAlreadyProcessedException ex, HttpServletRequest request) 
    {
    	ApiErrorResponse response = buildResponse(false, ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentProcessingException(PaymentProcessingException ex, HttpServletRequest request) 
    {
        ApiErrorResponse response = buildResponse(false, ex.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    /*
     * ==============================
     * COMMON RESPONSE BUILDER
     * ==============================
     */
    private ApiErrorResponse buildResponse(boolean success, String message) 
    {
        return new ApiErrorResponse(success, message, LocalDateTime.now());
    }
}