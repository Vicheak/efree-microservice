package com.efree.order.api.repository;

import com.efree.order.api.entity.OrderUnauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderUnauthRepository extends JpaRepository<OrderUnauth, String>, JpaSpecificationExecutor<OrderUnauth> {



}
