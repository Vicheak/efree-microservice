package com.efree.order.api.repository;

import com.efree.order.api.entity.OrderUnauthDetail;
import com.efree.order.api.entity.primarykey.OrderUnauthDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderUnauthDetailRepository extends JpaRepository<OrderUnauthDetail, OrderUnauthDetailId> {



}
