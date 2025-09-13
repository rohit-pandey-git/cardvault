package com.example.util;

import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionUtilTest {

    @Test
    void encryptAndDecryptShouldReturnOriginalText() throws Exception {
        String originalText = "SensitiveData";
        byte[] iv = new byte[12];
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();

        byte[] encryptedData = EncryptionUtil.encrypt(originalText, iv, secretKey);
        String decryptedText = EncryptionUtil.decrypt(encryptedData, iv, secretKey);

        assertEquals(originalText, decryptedText);
    }

    @Test
    void encryptShouldThrowExceptionForNullText() throws NoSuchAlgorithmException {
        byte[] iv = new byte[12];
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();

        assertThrows(Exception.class, () -> EncryptionUtil.encrypt(null, iv, secretKey));
    }

    @Test
    void decryptShouldThrowExceptionForInvalidData() throws Exception {
        byte[] invalidData = "InvalidData".getBytes(StandardCharsets.UTF_8); // Not valid encrypted data
        byte[] iv = new byte[12];
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();

        assertThrows(Exception.class, () -> EncryptionUtil.decrypt(invalidData, iv, secretKey));
    }

    @Test
    void encryptShouldThrowExceptionForNullKey() {
        String text = "SensitiveData";
        byte[] iv = new byte[12];

        assertThrows(Exception.class, () -> EncryptionUtil.encrypt(text, iv, null));
    }

    @Test
    void decryptShouldThrowExceptionForNullKey() {
        byte[] encryptedData = new byte[16];
        byte[] iv = new byte[12];

        assertThrows(Exception.class, () -> EncryptionUtil.decrypt(encryptedData, iv, null));
    }


    @Test
    void encryptShouldProduceDifferentResultsForDifferentIVs() throws Exception {
        String text = "SensitiveData";
        byte[] iv1 = new byte[12];
        byte[] iv2 = new byte[12];
        iv1[0] = 0;
        iv2[0] = 1; // Ensure IVs are different
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();

        byte[] encryptedData1 = EncryptionUtil.encrypt(text, iv1, secretKey);
        byte[] encryptedData2 = EncryptionUtil.encrypt(text, iv2, secretKey);

        assertFalse(Arrays.equals(encryptedData1, encryptedData2));
    }
}