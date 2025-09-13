package com.example.model;

import java.time.Instant;

public class CardResponse {
    private final String cardholderName;
    private final String maskedPan;
    private final Instant createdAt;

    public CardResponse(String cardholderName, String maskedPan, Instant createdAt) {
        this.cardholderName = cardholderName;
        this.maskedPan = maskedPan;
        this.createdAt = createdAt;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public String getMaskedPan() {
        return maskedPan;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}