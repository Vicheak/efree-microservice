package com.efree.order.api.external.paymentservice;

import com.efree.order.api.base.BaseApi;
import com.efree.order.api.external.paymentservice.dto.PaymentRequest;
import com.efree.order.api.external.paymentservice.dto.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service"/*, url = "${rest.payment-service.base-url}"*/)
public interface PaymentServiceRestClient {

    @PostMapping("/api/v1/payments")
    BaseApi<PaymentResponse> proceedPayment(@RequestBody @Valid PaymentRequest paymentRequest);

}
