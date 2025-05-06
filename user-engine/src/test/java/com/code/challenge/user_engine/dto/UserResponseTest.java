package com.code.challenge.user_engine.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class UserResponseTest {

    @Test
    void testNoArgsConstructor() {
        UserResponse response = new UserResponse();
        assertNotNull(response);
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime lastLogin = LocalDateTime.now();
        String token = "test-token";
        boolean isActive = true;
        String name = "Test User";
        String email = "test@example.com";
        String password = "password";
        List<PhoneDto> phones = List.of(new PhoneDto());

        UserResponse response = new UserResponse(id, created, lastLogin, token, isActive, name, email, password, phones);

        assertEquals(id, response.getId());
        assertEquals(created, response.getCreated());
        assertEquals(lastLogin, response.getLastLogin());
        assertEquals(token, response.getToken());
        assertEquals(isActive, response.isActive());
        assertEquals(name, response.getName());
        assertEquals(email, response.getEmail());
        assertEquals(password, response.getPassword());
        assertEquals(phones, response.getPhones());
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now();
        String name = "Builder Test";

        UserResponse response = UserResponse.builder()
                .id(id)
                .created(created)
                .name(name)
                .build();

        assertEquals(id, response.getId());
        assertEquals(created, response.getCreated());
        assertEquals(name, response.getName());
        assertNull(response.getToken());
        assertNull(response.getLastLogin());
        assertFalse(response.isActive());
    }

    @Test
    void testGetterSetterId() {
        UserResponse response = new UserResponse();
        UUID id = UUID.randomUUID();
        response.setId(id);
        assertEquals(id, response.getId());
    }

    @Test
    void testGetterSetterCreated() {
        UserResponse response = new UserResponse();
        LocalDateTime created = LocalDateTime.now();
        response.setCreated(created);
        assertEquals(created, response.getCreated());
    }

    @Test
    void testGetterSetterLastLogin() {
        UserResponse response = new UserResponse();
        LocalDateTime lastLogin = LocalDateTime.now();
        response.setLastLogin(lastLogin);
        assertEquals(lastLogin, response.getLastLogin());
    }

    @Test
    void testGetterSetterToken() {
        UserResponse response = new UserResponse();
        String token = "new-token";
        response.setToken(token);
        assertEquals(token, response.getToken());
    }

    @Test
    void testGetterSetterIsActive() {
        UserResponse response = new UserResponse();
        response.setActive(true);
        assertTrue(response.isActive());
        response.setActive(false);
        assertFalse(response.isActive());
    }

    @Test
    void testGetterSetterName() {
        UserResponse response = new UserResponse();
        String name = "John Doe";
        response.setName(name);
        assertEquals(name, response.getName());
    }

    @Test
    void testGetterSetterEmail() {
        UserResponse response = new UserResponse();
        String email = "john.doe@example.com";
        response.setEmail(email);
        assertEquals(email, response.getEmail());
    }

    @Test
    void testGetterSetterPassword() {
        UserResponse response = new UserResponse();
        String password = "securePassword123";
        response.setPassword(password);
        assertEquals(password, response.getPassword());
    }

    @Test
    void testGetterSetterPhones() {
        UserResponse response = new UserResponse();
        List<PhoneDto> phones = List.of(new PhoneDto(), new PhoneDto());
        response.setPhones(phones);
        assertEquals(2, response.getPhones().size());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        UserResponse response1 = UserResponse.builder().id(id).name("Test").build();
        UserResponse response2 = UserResponse.builder().id(id).name("Test").build();

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testToString() {
        UserResponse response = UserResponse.builder()
                .id(UUID.randomUUID())
                .name("ToString Test")
                .build();

        String toString = response.toString();
        assertTrue(toString.contains("ToString Test"));
        assertTrue(toString.contains("id="));
    }

    @Test
    void testCanEqual() {
        UserResponse response1 = new UserResponse();
        UserResponse response2 = new UserResponse();
        assertTrue(response1.canEqual(response2));
    }
}
