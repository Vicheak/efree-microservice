package com.efree.order.api.dto.mapper;

import com.efree.order.api.entity.Payment;
import com.efree.order.api.external.paymentservice.dto.PaymentResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentResponse mapFromPaymentToPaymentResponse(Payment payment);

    List<com.efree.order.api.dto.response.PaymentResponse> mapFromPaymentToPaymentResponse(List<Payment> payments);

}
