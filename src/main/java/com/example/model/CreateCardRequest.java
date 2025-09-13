package com.example.model;

import jakarta.validation.constraints.NotBlank;

public class CreateCardRequest {
    @NotBlank
    private String cardholderName;

    @NotBlank
    private String pan;

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
}