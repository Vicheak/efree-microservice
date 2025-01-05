package com.efree.payment.api.repository;

import com.efree.payment.api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {



}
