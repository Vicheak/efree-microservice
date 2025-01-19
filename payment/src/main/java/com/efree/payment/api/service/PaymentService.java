package com.efree.payment.api.service;

import com.efree.payment.api.dto.request.PaymentRequest;
import com.efree.payment.api.dto.request.ValidatePaymentRequest;
import com.efree.payment.api.dto.response.PaymentResponse;

public interface PaymentService {

    /**
     * This method is used to proceed payment to payment gateway system
     * @param paymentRequest is the payment request from client
     * @return PaymentResponse
     */
    PaymentResponse proceedPayment(PaymentRequest paymentRequest);

    /**
     * This method is used to validate payment request and payment status
     * @param validatePaymentRequest is the request to authorize from client
     */
    void validatePayment(ValidatePaymentRequest validatePaymentRequest);

}
