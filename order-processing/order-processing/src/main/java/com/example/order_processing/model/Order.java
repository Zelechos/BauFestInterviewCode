package com.example.order_processing.model;

import lombok.Data;

import java.util.List;


@Data
public class Order {
    private String orderId;
    private String customerId;
    private double orderAmount;
    private List<OrderItem> orderItems;
}

