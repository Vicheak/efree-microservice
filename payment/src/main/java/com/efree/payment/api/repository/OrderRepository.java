package com.efree.payment.api.repository;

import com.efree.payment.api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {



}
