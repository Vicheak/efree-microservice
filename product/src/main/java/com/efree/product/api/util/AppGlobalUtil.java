package com.efree.product.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;

public class AppGlobalUtil {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static <T> T convertStringToObject(String jsonString, Class<T> type) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, type);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public static String generatePromoCode(String prefix, int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Promo code length must be greater than 0.");
        }

        StringBuilder promoCode = new StringBuilder();

        // Add prefix if provided
        if (prefix != null && !prefix.isEmpty()) {
            promoCode.append(prefix.toUpperCase()).append("-");
        }

        // Generate random alphanumeric string
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            promoCode.append(CHARACTERS.charAt(index));
        }

        return promoCode.toString();
    }

}
