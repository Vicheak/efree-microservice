package com.efree.order.api.util;

public enum EOrderStatus {

    SUCCESS,
    FAIL,
    PREPARED,
    DELIVERED;

    public static boolean isValidOrderStatus(String status) {
        if (status == null) return false;
        try {
            EOrderStatus.valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
