package com.example.model;

import java.time.Instant;

public class ErrorResponse {
    private final String error;
    private final String message;
    private final int status;
    private final Instant timestamp;

    public ErrorResponse(String error, String message, int status) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = Instant.now();
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}