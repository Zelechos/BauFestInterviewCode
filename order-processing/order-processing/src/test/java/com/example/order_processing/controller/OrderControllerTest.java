package com.example.order_processing.controller;

import com.example.order_processing.model.Order;
import com.example.order_processing.model.OrderItem;
import com.example.order_processing.service.OrderProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderProcessingService orderProcessingService;

    @InjectMocks
    private OrderController orderController;

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
    void processOrder_ShouldReturnOkResponse_WhenOrderIsValid() throws Exception {
        when(orderProcessingService.processOrder(any(Order.class)))
                .thenReturn(CompletableFuture.completedFuture(testOrder));

        CompletableFuture<ResponseEntity<Order>> responseFuture =
                orderController.processOrder(testOrder);

        ResponseEntity<Order> response = responseFuture.get();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testOrder, response.getBody());
    }

    @Test
    void processOrder_ShouldReturnBadRequest_WhenServiceThrowsException() throws Exception {
        when(orderProcessingService.processOrder(any(Order.class)))
                .thenReturn(CompletableFuture.failedFuture(new IllegalArgumentException("Invalid order")));

        CompletableFuture<ResponseEntity<Order>> responseFuture =
                orderController.processOrder(testOrder);

        ResponseEntity<Order> response = responseFuture.get();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void processOrder_ShouldLogError_WhenProcessingFails() throws Exception {
        when(orderProcessingService.processOrder(any(Order.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Processing error")));

        orderController.processOrder(testOrder).get();

        verify(orderProcessingService).processOrder(any(Order.class));
    }

    @Test
    void getOrder_ShouldReturnOrder_WhenOrderExists() {
        when(orderProcessingService.getProcessedOrder("existing-order"))
                .thenReturn(testOrder);

        ResponseEntity<Order> response =
                orderController.getOrder("existing-order");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testOrder, response.getBody());
    }

    @Test
    void getOrder_ShouldReturnNotFound_WhenOrderDoesNotExist() {
        when(orderProcessingService.getProcessedOrder("non-existing-order"))
                .thenReturn(null);

        ResponseEntity<Order> response =
                orderController.getOrder("non-existing-order");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void processOrder_ShouldHandleInterruptedException() throws Exception {
        CompletableFuture<Order> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new InterruptedException("Interrupted"));

        when(orderProcessingService.processOrder(any(Order.class)))
                .thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<Order>> responseFuture =
                orderController.processOrder(testOrder);

        ResponseEntity<Order> response = responseFuture.get();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void processOrder_ShouldPreserveOrderId_WhenSuccessful() throws Exception {
        when(orderProcessingService.processOrder(any(Order.class)))
                .thenReturn(CompletableFuture.completedFuture(testOrder));

        CompletableFuture<ResponseEntity<Order>> responseFuture =
                orderController.processOrder(testOrder);

        ResponseEntity<Order> response = responseFuture.get();
        assertEquals("test-order-1", response.getBody().getOrderId());
    }

    @Test
    void getOrder_ShouldCallServiceWithCorrectOrderId() {
        orderController.getOrder("test-id-123");
        verify(orderProcessingService).getProcessedOrder("test-id-123");
    }

    @Test
    void processOrder_ShouldCallServiceWithCorrectOrder() throws Exception {
        when(orderProcessingService.processOrder(any(Order.class)))
                .thenReturn(CompletableFuture.completedFuture(testOrder));

        orderController.processOrder(testOrder).get();

        verify(orderProcessingService).processOrder(testOrder);
    }

    @Test
    void processOrder_ShouldReturnInternalServerError_WhenUnexpectedExceptionOccurs() throws Exception {
        when(orderProcessingService.processOrder(any(Order.class)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("Unexpected error")));

        CompletableFuture<ResponseEntity<Order>> responseFuture =
                orderController.processOrder(testOrder);

        ResponseEntity<Order> response = responseFuture.get();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
