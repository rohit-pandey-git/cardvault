package com.example.util;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;

public class EncryptionUtil {

    public static byte[] encrypt(String text, byte[] iv, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
        return cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
    }

    //Thus method is not used in the application but provided for completeness and testing
    //Without a decryption functionality, encryption cannot be tested
    //The util has it but this method is not used in service layer
    public static String decrypt(byte[] encryptedData, byte[] iv, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}