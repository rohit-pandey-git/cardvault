package com.example.util;

public class PanValidator {
    public static boolean isValid(String pan) {
        if (pan.length() < 12 || pan.length() > 19 || !pan.matches("\\d+")) return false;
        return luhnCheck(pan);
    }

    private static boolean luhnCheck(String pan) {
        int sum = 0;
        boolean alternate = false;
        for (int i = pan.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(pan.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}