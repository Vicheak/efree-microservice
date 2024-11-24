package com.efree.gateway.exception;

import com.efree.gateway.base.BaseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class WebClientRequestException {

    @ExceptionHandler(WebClientException.class)
    public ResponseEntity<?> handleFeignException(WebClientException ex) {
        return new ResponseEntity<>(BaseError.builder()
                .isSuccess(false)
                .message("Service is busy, please check...!")
                .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                .timestamp(LocalDateTime.now())
                .errors("Something went wrong with the internal services!")
                .build(), HttpStatus.SERVICE_UNAVAILABLE);
    }

}
