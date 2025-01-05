package com.efree.payment.api.service;

import com.efree.payment.api.dto.request.PaymentRequest;
import com.efree.payment.api.dto.request.ValidatePaymentRequest;
import com.efree.payment.api.dto.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse proceedPayment(PaymentRequest paymentRequest);

    void validatePayment(ValidatePaymentRequest validatePaymentRequest);

}
