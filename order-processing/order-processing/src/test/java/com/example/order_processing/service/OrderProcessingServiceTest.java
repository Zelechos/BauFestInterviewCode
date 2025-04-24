package com.example.order_processing.service;

import com.example.order_processing.model.Order;
import com.example.order_processing.model.OrderItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderProcessingServiceTest {

    @InjectMocks
    private OrderProcessingService orderProcessingService;

    @Test
    void processOrder_ShouldCalculateAmount_WhenNotProvided() throws Exception {
        Order order = new Order();
        order.setOrderId("test-1");
        order.setOrderItems(List.of(
                new OrderItem("prod-1", 2, 10.0),
                new OrderItem("prod-2", 1, 15.0)
        ));

        CompletableFuture<Order> future = orderProcessingService.processOrder(order);
        Order result = future.get();

        assertEquals(35.0, result.getOrderAmount());
    }

    @Test
    void processOrder_ShouldThrowException_WhenOrderIdIsMissing() {
        Order order = new Order();
        order.setOrderItems(List.of(new OrderItem("prod-1", 1, 10.0)));

        CompletableFuture<Order> future = orderProcessingService.processOrder(order);

        assertThrows(ExecutionException.class, () -> {
            try {
                future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Thread was interrupted");
            }
        });
    }

    @Test
    void processOrder_ShouldUseProvidedAmount_WhenAlreadySet() throws Exception {
        Order order = new Order();
        order.setOrderId("test-2");
        order.setOrderAmount(50.0);
        order.setOrderItems(List.of(
                new OrderItem("prod-3", 1, 10.0)
        ));

        CompletableFuture<Order> future = orderProcessingService.processOrder(order);
        Order result = future.get();

        assertEquals(50.0, result.getOrderAmount(),
                "Debería mantener el monto predefinido");
    }

    @Test
    void processOrder_ShouldStoreOrderInProcessedOrders() throws Exception {
        Order order = new Order();
        order.setOrderId("test-3");
        order.setOrderItems(List.of(new OrderItem("prod-4", 3, 5.0)));

        CompletableFuture<Order> future = orderProcessingService.processOrder(order);
        future.get(); // Esperar a que se complete

        Order retrievedOrder = orderProcessingService.getProcessedOrder("test-3");
        assertNotNull(retrievedOrder, "El pedido debería estar almacenado");
        assertEquals(15.0, retrievedOrder.getOrderAmount());
    }

    @Test
    void getProcessedOrder_ShouldReturnNull_WhenOrderNotProcessed() {
        Order retrievedOrder = orderProcessingService.getProcessedOrder("non-existent");
        assertNull(retrievedOrder, "Debería retornar null para pedidos no existentes");
    }

    @Test
    void processOrder_ShouldHandleMultipleConcurrentRequests() throws Exception {
        int numberOfOrders = 100;
        List<CompletableFuture<Order>> futures = new ArrayList<>();

        for (int i = 0; i < numberOfOrders; i++) {
            Order order = new Order();
            order.setOrderId("concurrent-" + i);
            order.setOrderItems(List.of(new OrderItem("prod-" + i, 1, 10.0)));
            futures.add(orderProcessingService.processOrder(order));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get();

        for (int i = 0; i < numberOfOrders; i++) {
            assertNotNull(orderProcessingService.getProcessedOrder("concurrent-" + i),
                    "Todos los pedidos concurrentes deberían estar procesados");
        }
    }

}
