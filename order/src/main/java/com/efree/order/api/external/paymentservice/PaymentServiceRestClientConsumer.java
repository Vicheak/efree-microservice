package com.efree.order.api.external.paymentservice;

import com.efree.order.api.base.BaseApi;
import com.efree.order.api.entity.Order;
import com.efree.order.api.entity.Payment;
import com.efree.order.api.external.paymentservice.dto.PaymentRequest;
import com.efree.order.api.external.paymentservice.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceRestClientConsumer {

    private final PaymentServiceRestClient paymentServiceRestClient;

    public PaymentResponse proceedPayment(Order order, Payment payment) {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getOrderId())
                .paymentId(payment.getPaymentId())
                .paymentToken(payment.getPaymentToken())
                .orderRef(payment.getReference())
                .paymentAmount(payment.getAmount())
                .paymentCurrency("USD")
                .build();
        BaseApi<PaymentResponse> paymentResponseBaseApi =
                paymentServiceRestClient.proceedPayment(paymentRequest);
        return paymentResponseBaseApi.payload();
    }

}
