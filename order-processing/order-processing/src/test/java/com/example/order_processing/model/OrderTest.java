package com.example.order_processing.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class OrderTest {

    @Test
    void testOrderIdGetterAndSetter() {
        Order order = new Order();
        String testOrderId = "ORD-12345";
        order.setOrderId(testOrderId);

        assertEquals(testOrderId, order.getOrderId());
    }

    @Test
    void testCustomerIdGetterAndSetter() {
        Order order = new Order();
        String testCustomerId = "CUST-1001";
        order.setCustomerId(testCustomerId);

        assertEquals(testCustomerId, order.getCustomerId());
    }

    @Test
    void testOrderAmountGetterAndSetter() {
        Order order = new Order();
        double testAmount = 99.99;
        order.setOrderAmount(testAmount);

        assertEquals(testAmount, order.getOrderAmount(), 0.001);
    }

    @Test
    void testOrderItemsGetterAndSetter() {
        Order order = new Order();
        OrderItem item1 = new OrderItem("PROD-001", 2, 10.0);
        OrderItem item2 = new OrderItem("PROD-002", 1, 15.0);
        List<OrderItem> testItems = List.of(item1, item2);

        order.setOrderItems(testItems);

        assertEquals(2, order.getOrderItems().size());
        assertEquals("PROD-001", order.getOrderItems().get(0).getProductId());
        assertEquals("PROD-002", order.getOrderItems().get(1).getProductId());
    }

    @Test
    void testEqualsAndHashCode() {
        Order order1 = new Order();
        order1.setOrderId("ORD-100");
        order1.setCustomerId("CUST-200");

        Order order2 = new Order();
        order2.setOrderId("ORD-100");
        order2.setCustomerId("CUST-200");

        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());
    }

    @Test
    void testNotEqualsWithDifferentIds() {
        Order order1 = new Order();
        order1.setOrderId("ORD-100");

        Order order2 = new Order();
        order2.setOrderId("ORD-101");

        assertNotEquals(order1, order2);
    }

    @Test
    void testToStringContainsOrderDetails() {
        Order order = new Order();
        order.setOrderId("ORD-500");
        order.setCustomerId("CUST-600");
        order.setOrderAmount(150.75);

        String toStringResult = order.toString();

        assertTrue(toStringResult.contains("ORD-500"));
        assertTrue(toStringResult.contains("CUST-600"));
        assertTrue(toStringResult.contains("150.75"));
    }
}