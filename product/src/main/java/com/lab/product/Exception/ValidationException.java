package com.lab.product.Exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}