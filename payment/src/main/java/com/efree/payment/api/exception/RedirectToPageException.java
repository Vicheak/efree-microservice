package com.efree.payment.api.exception;

import lombok.Getter;

@Getter
public class RedirectToPageException extends RuntimeException {

    private final String page;

    public RedirectToPageException(String msgName) {
        this.page = msgName;
    }

}