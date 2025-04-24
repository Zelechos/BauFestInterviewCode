package com.example.order_processing;

import com.example.order_processing.model.Order;
import com.example.order_processing.model.OrderItem;
import com.example.order_processing.service.OrderProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ConcurrencyTest {

    @Autowired
    private OrderProcessingService orderProcessingService;

    @Test
    void testHighConcurrency() throws InterruptedException, ExecutionException {
        int numberOfOrders = 1000;
        List<CompletableFuture<Order>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfOrders; i++) {
            Order order = new Order();
            order.setOrderId("order-" + i);
            order.setCustomerId("customer-" + (i % 100));
            order.setOrderItems(List.of(
                    new OrderItem("prod-" + (i % 50), 1, 10.0)
            ));

            futures.add(orderProcessingService.processOrder(order));
        }

        // Esperar a que todos los pedidos se completen
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();

        // Verificar que todos los pedidos fueron procesados
        for (int i = 0; i < numberOfOrders; i++) {
            assertNotNull(orderProcessingService.getProcessedOrder("order-" + i));
        }
    }
}
