package com.example.order_processing.service;

import com.example.order_processing.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class ReactiveOrderProcessingService {
    private final Map<String, Order> processedOrders = new ConcurrentHashMap<>();

    public Mono<Order> processOrderReactive(Order order) {
        return Mono.fromCallable(() -> {
                    // Simular procesamiento con retraso aleatorio
                    int delay = ThreadLocalRandom.current().nextInt(100, 500);
                    Thread.sleep(delay);

                    if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
                        throw new IllegalArgumentException("Order ID is required");
                    }

                    if (order.getOrderAmount() <= 0) {
                        order.setOrderAmount(
                                order.getOrderItems().stream()
                                        .mapToDouble(item -> item.getPrice() * item.getQuantity())
                                        .sum()
                        );
                    }

                    processedOrders.put(order.getOrderId(), order);
                    log.info("Processed order {} in {} ms", order.getOrderId(), delay);
                    return order;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofSeconds(2))
                .doOnError(e -> log.error("Error processing order: {}", e.getMessage()));
    }
}


