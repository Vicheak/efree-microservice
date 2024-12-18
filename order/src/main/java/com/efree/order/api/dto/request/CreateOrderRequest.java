package com.efree.order.api.dto.request;

import com.efree.order.api.entity.CustomerPayment;
import com.efree.order.api.entity.Order;
import com.efree.order.api.entity.OrderDetail;

import java.util.List;

public class CreateOrderRequest {
    private Order order;
    private List<OrderDetail> orderDetails;
    private CustomerPayment payment;

    // Getters and Setters
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public CustomerPayment getPayment() {
        return payment;
    }

    public void setPayment(CustomerPayment payment) {
        this.payment = payment;
    }
}

