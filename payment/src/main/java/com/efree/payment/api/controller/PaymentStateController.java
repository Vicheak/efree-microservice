package com.efree.payment.api.controller;

import com.efree.payment.api.dto.request.ValidatePaymentRequest;
import com.efree.payment.api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentStateController {

    private final PaymentService paymentService;

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("orderId") String orderId){
        ValidatePaymentRequest validatePaymentRequest = ValidatePaymentRequest.builder()
                .isPaymentSuccess(true)
                .orderId(orderId)
                .build();
        paymentService.validatePayment(validatePaymentRequest);
        return "payment-success";
    }

    @GetMapping("/cancel")
    public String paymentCancel(@RequestParam("orderId") String orderId){
        ValidatePaymentRequest validatePaymentRequest = ValidatePaymentRequest.builder()
                .isPaymentSuccess(false)
                .orderId(orderId)
                .build();
        paymentService.validatePayment(validatePaymentRequest);
        return "payment-cancel";
    }

}