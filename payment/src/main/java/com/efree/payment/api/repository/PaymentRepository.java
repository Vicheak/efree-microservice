package com.efree.payment.api.repository;

import com.efree.payment.api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findByPaymentIdAndPaymentToken(String paymentId, String paymentToken);

}
