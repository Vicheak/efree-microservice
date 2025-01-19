package com.efree.order.api.repository;

import com.efree.order.api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findByOrderOrderId(String orderId);

}
