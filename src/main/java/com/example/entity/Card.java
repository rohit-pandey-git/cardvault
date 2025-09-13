package com.example.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "cards", uniqueConstraints = {@UniqueConstraint(name = "uc_pan_hash", columnNames = {"panHash"})},
        indexes = {@Index(name = "idx_last4_hash", columnList = "last4Hash")
        })
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardholderName;

    @Lob
    private byte[] encryptedPan;

    private byte[] iv;

    private String panHash;

    private String last4Hash;

    private String maskedPan;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private String keyVersion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public byte[] getEncryptedPan() {
        return encryptedPan;
    }

    public void setEncryptedPan(byte[] encryptedPan) {
        this.encryptedPan = encryptedPan;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

    public String getPanHash() {
        return panHash;
    }

    public void setPanHash(String panHash) {
        this.panHash = panHash;
    }

    public String getLast4Hash() {
        return last4Hash;
    }

    public void setLast4Hash(String last4Hash) {
        this.last4Hash = last4Hash;
    }

    public String getMaskedPan() {
        return maskedPan;
    }

    public void setMaskedPan(String maskedPan) {
        this.maskedPan = maskedPan;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getKeyVersion() {
        return keyVersion;
    }

    public void setKeyVersion(String keyVersion) {
        this.keyVersion = keyVersion;
    }
}