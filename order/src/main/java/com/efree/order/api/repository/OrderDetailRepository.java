package com.efree.order.api.repository;

import com.efree.order.api.entity.OrderDetail;
import com.efree.order.api.entity.primarykey.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailId> {



}
