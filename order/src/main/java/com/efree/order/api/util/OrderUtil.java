package com.efree.order.api.util;

import java.security.SecureRandom;
import java.util.Base64;

public class OrderUtil {

    public static String generatePaymentToken() {
        // Define the length of the token (e.g., 64 characters)
        int tokenLength = 64;

        // Create a SecureRandom instance
        SecureRandom secureRandom = new SecureRandom();

        // Create a byte array to hold the random bytes
        byte[] randomBytes = new byte[tokenLength];

        // Fill the byte array with random bytes
        secureRandom.nextBytes(randomBytes);

        // Encode the random bytes to a Base64 URL-safe string
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

}
