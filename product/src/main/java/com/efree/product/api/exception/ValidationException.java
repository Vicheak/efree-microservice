package com.efree.product.api.exception;

import com.efree.product.api.base.BaseError;
import com.efree.product.api.base.FieldError;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ValidationException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseError<?> handleValidationException(MethodArgumentNotValidException ex) {
        List<FieldError> errors = new ArrayList<>();

        ex.getFieldErrors().forEach(fieldError -> errors.add(FieldError.builder()
                .fieldName(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .build()));

        return BaseError.builder()
                .isSuccess(false)
                .message("Validation has been errored, please check...!")
                .code(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public BaseError<?> handleInvalidArgException(IllegalArgumentException ex) {
        return BaseError.builder()
                .isSuccess(false)
                .message("Invalid request has been errored, please check...!")
                .code(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(ex.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PropertyReferenceException.class)
    public BaseError<?> handlePropertyReferenceException(PropertyReferenceException ex) {
        return BaseError.builder()
                .isSuccess(false)
                .message("Property request has been errored, please check...!")
                .code(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .errors(ex.getMessage())
                .build();
    }

}