package com.example.order_processing.service;

import com.example.order_processing.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class OrderProcessingService {
    private final Map<String, Order> processedOrders = new ConcurrentHashMap<>();

    public CompletableFuture<Order> processOrder(Order order) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Simular procesamiento con retraso aleatorio
                int delay = ThreadLocalRandom.current().nextInt(100, 500);
                Thread.sleep(delay);

                // Validación básica
                if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
                    throw new IllegalArgumentException("Order ID is required");
                }

                // Calcular el monto total si no está proporcionado
                if (order.getOrderAmount() <= 0) {
                    order.setOrderAmount(
                            order.getOrderItems().stream()
                                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                                    .sum()
                    );
                }

                // Almacenar el pedido procesado
                processedOrders.put(order.getOrderId(), order);

                log.info("Order {} processed successfully in {} ms", order.getOrderId(), delay);
                return order;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Order processing interrupted", e);
            }
        });
    }

    public Order getProcessedOrder(String orderId) {
        return processedOrders.get(orderId);
    }
}
