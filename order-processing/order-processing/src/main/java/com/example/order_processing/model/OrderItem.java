package com.example.order_processing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String productId;
    private int quantity;
    private double price;
}
