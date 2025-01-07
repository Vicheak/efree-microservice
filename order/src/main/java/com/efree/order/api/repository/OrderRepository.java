package com.efree.order.api.repository;

import com.efree.order.api.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {



}
