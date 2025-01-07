package com.efree.order.api.repository;

import com.efree.order.api.entity.OrderUnauth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderUnauthRepository extends JpaRepository<OrderUnauth, String> {



}
