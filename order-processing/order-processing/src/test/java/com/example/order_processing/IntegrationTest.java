package com.example.order_processing;

import com.example.order_processing.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;
import com.example.order_processing.model.OrderItem;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void whenProcessOrder_thenReturnsOrder() {
        Order order = new Order();
        order.setOrderId("int-test-1");
        order.setOrderItems(List.of(new OrderItem("prod-1", 2, 10.0)));

        ResponseEntity<Order> response = restTemplate.postForEntity(
                "/api/orders/processOrder",
                order,
                Order.class
        );

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(20.0, response.getBody().getOrderAmount());
    }
}
