package com.example.exception;

public class InvalidPanException extends RuntimeException {
    public InvalidPanException(String message) {
        super(message);
    }
}