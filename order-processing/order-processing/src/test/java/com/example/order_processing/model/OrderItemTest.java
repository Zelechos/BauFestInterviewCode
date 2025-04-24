package com.example.order_processing.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void testAllArgsConstructor() {
        OrderItem item = new OrderItem("PROD-001", 3, 15.99);

        assertEquals("PROD-001", item.getProductId());
        assertEquals(3, item.getQuantity());
        assertEquals(15.99, item.getPrice(), 0.001);
    }

    @Test
    void testRequiredArgsConstructor() {
        OrderItem item = new OrderItem();
        item.setProductId("PROD-002");
        item.setQuantity(2);
        item.setPrice(9.99);

        assertEquals("PROD-002", item.getProductId());
        assertEquals(2, item.getQuantity());
        assertEquals(9.99, item.getPrice(), 0.001);
    }

    @Test
    void testEqualsAndHashCode() {
        OrderItem item1 = new OrderItem("PROD-100", 1, 10.0);
        OrderItem item2 = new OrderItem("PROD-100", 1, 10.0);

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testNotEqualsWithDifferentProductId() {
        OrderItem item1 = new OrderItem("PROD-101", 1, 10.0);
        OrderItem item2 = new OrderItem("PROD-102", 1, 10.0);

        assertNotEquals(item1, item2);
    }

    @Test
    void testNotEqualsWithDifferentQuantity() {
        OrderItem item1 = new OrderItem("PROD-101", 1, 10.0);
        OrderItem item2 = new OrderItem("PROD-101", 2, 10.0);

        assertNotEquals(item1, item2);
    }

    @Test
    void testNotEqualsWithDifferentPrice() {
        OrderItem item1 = new OrderItem("PROD-101", 1, 10.0);
        OrderItem item2 = new OrderItem("PROD-101", 1, 10.5);

        assertNotEquals(item1, item2);
    }

    @Test
    void testToStringContainsAllFields() {
        OrderItem item = new OrderItem("PROD-999", 5, 25.75);
        String toStringResult = item.toString();

        assertTrue(toStringResult.contains("PROD-999"));
        assertTrue(toStringResult.contains("5"));
        assertTrue(toStringResult.contains("25.75"));
    }
}