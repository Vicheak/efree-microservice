package com.efree.payment.api.controller;

import com.efree.payment.api.base.BaseApi;
import com.efree.payment.api.dto.request.PaymentRequest;
import com.efree.payment.api.dto.response.PaymentResponse;
import com.efree.payment.api.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BaseApi<?> createNewCategory(@RequestBody @Valid PaymentRequest paymentRequest) {

        PaymentResponse paymentResponse = paymentService.proceedPayment(paymentRequest);

        return BaseApi.builder()
                .isSuccess(true)
                .code(HttpStatus.CREATED.value())
                .message("Proceed payment successfully!")
                .timestamp(LocalDateTime.now())
                .payload(paymentResponse)
                .build();
    }

}
