package com.efree.payment.api.service.impl;

import com.efree.payment.api.dto.request.PaymentRequest;
import com.efree.payment.api.dto.request.ValidatePaymentRequest;
import com.efree.payment.api.dto.response.PaymentResponse;
import com.efree.payment.api.entity.Order;
import com.efree.payment.api.exception.RedirectToPageException;
import com.efree.payment.api.repository.OrderRepository;
import com.efree.payment.api.repository.PaymentRepository;
import com.efree.payment.api.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Value("${stripe.secretKey}")
    private String secretKey;
    @Value("${stripe.success-url}")
    private String paymentSuccessUrl;
    @Value("${stripe.cancel-url}")
    private String paymentCancelUrl;

    @Override
    public PaymentResponse proceedPayment(PaymentRequest paymentRequest) {
        // Set your secret key, switch to live secret key in production for real payment
        Stripe.apiKey = secretKey;

        // Calculate unit amount
        Long paymentAmount = paymentRequest.paymentCurrency().equals("USD") ?
                paymentRequest.paymentAmount().longValue() * 100 :
                paymentRequest.paymentAmount().longValue();

        // Create a PaymentIntent with the order amount and currency
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(paymentRequest.orderRef())
                        .setDescription("Payment for a complete order")
                        .build();

        // Create new line item with the above product data and associated price
        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(paymentRequest.paymentCurrency())
                        .setUnitAmount(paymentAmount)
                        .setProductData(productData)
                        .build();

        // Create new line item with the above price data
        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L) // one payment one order
                        .setPriceData(priceData)
                        .build();

        // Create new session with the line items
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(paymentSuccessUrl + "?orderId=" + paymentRequest.orderId())
                        .setCancelUrl(paymentCancelUrl + "?orderId=" + paymentRequest.orderId())
                        .addLineItem(lineItem)
                        .build();

        // Create new session
        Session session;
        try {
            session = Session.create(params);
        } catch (StripeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        // Prepare response data
        String message =
                session.getRedirectOnCompletion() == null ?
                        "Payment request redirection" :
                        session.getRedirectOnCompletion();

        return PaymentResponse.builder()
                .status(session.getPaymentStatus())
                .message(message)
                .paymentSessionId(session.getId())
                .paymentSessionUrl(session.getUrl())
                .expiresAt(session.getExpiresAt())
                .paymentIntent(session.getPaymentIntent() != null ? session.getPaymentIntent() : "")
                .currency(session.getCurrency().toUpperCase())
                .allowPromotionCode(session.getAllowPromotionCodes() != null ? session.getAllowPromotionCodes() : false)
                .clientReferenceId(session.getClientReferenceId() != null ? session.getClientReferenceId() : "")
                .customer(session.getCustomer() != null ? session.getCustomer() : "")
                .customerEmail(session.getCustomerEmail() != null ? session.getCustomerEmail() : "")
                .invoice(session.getInvoice() != null ? session.getInvoice() : "")
                .billingAddress(session.getBillingAddressCollection() != null ? session.getBillingAddressCollection() : "")
                .shippingDetail("")
                .shippingCost(0L)
                .submitType(session.getSubmitType() != null ? session.getSubmitType() : "API")
                .mode(session.getMode())
                .build();
    }

    @Override
    public void validatePayment(ValidatePaymentRequest validatePaymentRequest) {
        Optional<Order> orderOptional = orderRepository.findById(validatePaymentRequest.orderId());
        if(orderOptional.isEmpty()){
            throw new RedirectToPageException("order-not-found");
        }
    }

}
