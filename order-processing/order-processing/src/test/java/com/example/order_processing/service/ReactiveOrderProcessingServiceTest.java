package com.example.order_processing.service;

import com.example.order_processing.model.Order;
import com.example.order_processing.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReactiveOrderProcessingServiceTest {

    @InjectMocks
    private ReactiveOrderProcessingService service;

    @Mock
    private Logger log;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setOrderId("test-order-1");
        testOrder.setOrderItems(List.of(
                new OrderItem("prod-1", 2, 10.0),
                new OrderItem("prod-2", 1, 15.0)
        ));
    }

    @Test
    void processOrderReactive_ShouldProcessValidOrderSuccessfully() {
        StepVerifier.create(service.processOrderReactive(testOrder))
                .expectNextMatches(order -> {
                    assertEquals(35.0, order.getOrderAmount());
                    assertEquals("test-order-1", order.getOrderId());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void processOrderReactive_ShouldCalculateAmountWhenNotProvided() {
        testOrder.setOrderAmount(0);

        StepVerifier.create(service.processOrderReactive(testOrder))
                .expectNextMatches(order -> order.getOrderAmount() == 35.0)
                .verifyComplete();
    }

    @Test
    void processOrderReactive_ShouldUseProvidedAmountWhenPositive() {
        testOrder.setOrderAmount(50.0);

        StepVerifier.create(service.processOrderReactive(testOrder))
                .expectNextMatches(order -> order.getOrderAmount() == 50.0)
                .verifyComplete();
    }

    @Test
    void processOrderReactive_ShouldFailWhenOrderIdIsMissing() {
        testOrder.setOrderId(null);

        StepVerifier.create(service.processOrderReactive(testOrder))
                .verifyError(IllegalArgumentException.class);
    }

    @Test
    void processOrderReactive_ShouldProcessMultipleOrdersConcurrently() {
        int orderCount = 10;
        Mono<Order>[] monos = new Mono[orderCount];

        for (int i = 0; i < orderCount; i++) {
            Order order = new Order();
            order.setOrderId("order-" + i);
            order.setOrderItems(List.of(new OrderItem("prod-" + i, 1, 10.0)));
            monos[i] = service.processOrderReactive(order);
        }

        StepVerifier.create(Mono.when(monos))
                .verifyComplete();
    }

    @Test
    void processOrderReactive_ShouldNotExceedTimeoutDuration() {
        long startTime = System.currentTimeMillis();

        StepVerifier.create(service.processOrderReactive(testOrder))
                .expectNextCount(1)
                .verifyComplete();

        long duration = System.currentTimeMillis() - startTime;
        assertTrue(duration < 2500, "Processing took too long: " + duration + "ms");
    }
}
