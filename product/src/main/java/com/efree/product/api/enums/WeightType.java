package com.efree.product.api.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum WeightType {
    KG, G, MG;

    public static boolean isValid(String value) {
        return Arrays.stream(WeightType.values())
                .anyMatch(weightType -> weightType.name().equalsIgnoreCase(value));
    }

    public static String allowedValues() {
        return Arrays.stream(WeightType.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
