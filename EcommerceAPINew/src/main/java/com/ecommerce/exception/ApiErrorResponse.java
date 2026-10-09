package com.ecommerce.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiErrorResponse {

    private boolean success;
    private String message;
    private Map<String, String> validationErrors;
    private LocalDateTime timestamp;

    public ApiErrorResponse() {
    }

    public ApiErrorResponse(boolean success, String message, LocalDateTime timestamp) 
    {
        this.success = success;
        this.message = message;
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setValidationErrors(
            Map<String, String> validationErrors) {

        this.validationErrors = validationErrors;
    }
}