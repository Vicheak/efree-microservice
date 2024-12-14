package com.efree.user.api.exception;

import com.efree.user.api.base.BaseError;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class FeignClientException {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException ex) {
        if (ex instanceof FeignException.ServiceUnavailable ||
                ex.getMessage().contains("Connection refused")) { //@TODO : need to implement fallback mechanism
            return new ResponseEntity<>(BaseError.builder()
                    .isSuccess(false)
                    .message("Service is busy, please check...!")
                    .code(HttpStatus.SERVICE_UNAVAILABLE.value())
                    .timestamp(LocalDateTime.now())
                    .errors(ex.getMessage())
                    .build(), HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<>(BaseError.builder()
                .isSuccess(false)
                .message("Something went wrong, please check...!")
                .code(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

}
