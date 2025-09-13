package com.example.service;

import com.example.util.EncryptionUtil;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

@Service
public class EncryptionService {

    // In a real application, all versions of key would be soured from a secure vault
    // A single static key is assumed for simplicity.
    // The version is stored with other card details in database so that correct key can be used for decryption if needed
    private final String currentKeyVersion = "v1";;
    private final Map<String, SecretKey> keyRegistry = Map.of(
            currentKeyVersion, generateKey());

    public String getCurrentKeyVersion() {
        return currentKeyVersion;
    }

    private SecretKey getCurrentKey() {
        return keyRegistry.get(currentKeyVersion);
    }

    //Encryption is always done using "current" key
    public byte[] encrypt(String pan, byte[] iv) throws Exception {
        return EncryptionUtil.encrypt(pan, iv, getCurrentKey());
    }

    public byte[] generateIv() {
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public String hash(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return Base64.getEncoder().encodeToString(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
    }

    public String maskPan(String pan) {
        return pan.substring(0, 4) + " " + pan.substring(4, 6) + " ** " + pan.substring(pan.length() - 4);
    }

    private SecretKey generateKey() {
        byte[] keyBytes = new byte[16];
        new SecureRandom().nextBytes(keyBytes);
        return new SecretKeySpec(keyBytes, "AES");
    }
}