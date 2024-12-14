package com.efree.fileservice.exception;

import com.efree.fileservice.base.BaseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class FileErrorException {

    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public BaseError<?> handleFileErrorEx(MaxUploadSizeExceededException ex) {
        return BaseError.builder()
                .isSuccess(false)
                .message("Something went wrong!")
                .code(7000)
                .timestamp(LocalDateTime.now())
                .errors(ex.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IOException.class)
    public BaseError<?> handleIOEx(IOException ex) {
        return BaseError.builder()
                .isSuccess(false)
                .message("Something went wrong!")
                .code(7100)
                .timestamp(LocalDateTime.now())
                .errors(ex.getMessage())
                .build();
    }

}
