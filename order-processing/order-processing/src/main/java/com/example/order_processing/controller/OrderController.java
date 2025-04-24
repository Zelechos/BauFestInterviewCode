package com.example.order_processing.controller;

import com.example.order_processing.model.Order;
import com.example.order_processing.service.OrderProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private final OrderProcessingService orderProcessingService;

    @Autowired
    public OrderController(OrderProcessingService orderProcessingService) {
        this.orderProcessingService = orderProcessingService;
    }

    @PostMapping("/processOrder")
    public CompletableFuture<ResponseEntity<Order>> processOrder(@RequestBody Order order) {
        log.info("Received order processing request for order ID: {}", order.getOrderId());
        return orderProcessingService.processOrder(order)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    log.error("Error processing order: {}", ex.getMessage());
                    return ResponseEntity.badRequest().build();
                });
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        Order order = orderProcessingService.getProcessedOrder(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.notFound().build();
    }
}
