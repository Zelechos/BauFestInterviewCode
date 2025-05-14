package com.code.challenge.user_engine.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getPhones());
        assertNull(user.getCreated());
        assertNull(user.getLastLogin());
        assertNull(user.getToken());
        assertFalse(user.isActive());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        List<Phone> phones = List.of(new Phone());

        User user = new User(id, "Test User", "test@example.com", "password",
                phones, now, now, "token123", true);

        assertEquals(id, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(1, user.getPhones().size());
        assertEquals(now, user.getCreated());
        assertEquals(now, user.getLastLogin());
        assertEquals("token123", user.getToken());
        assertTrue(user.isActive());
    }

    @Test
    void testBuilder() {
        LocalDateTime created = LocalDateTime.now();
        User user = User.builder()
                .name("Builder Test")
                .email("builder@test.com")
                .password("secure123")
                .created(created)
                .isActive(false)
                .build();

        assertNull(user.getId());
        assertEquals("Builder Test", user.getName());
        assertEquals("builder@test.com", user.getEmail());
        assertEquals("secure123", user.getPassword());
        assertEquals(created, user.getCreated());
        assertFalse(user.isActive());
    }

    @Test
    void testJpaAnnotations() {
        // Verify class-level annotations
        assertNotNull(User.class.getAnnotation(Entity.class));
        Table table = User.class.getAnnotation(Table.class);
        assertNotNull(table);
        assertEquals("users", table.name());

        // Verify field annotations
        try {
            // Check ID field annotations
            assertNotNull(User.class.getDeclaredField("id").getAnnotation(Id.class));
            GeneratedValue generatedValue = User.class.getDeclaredField("id").getAnnotation(GeneratedValue.class);
            assertNotNull(generatedValue);
            assertEquals("UUID", generatedValue.generator());

            // Check email field annotations
            Column emailColumn = User.class.getDeclaredField("email").getAnnotation(Column.class);
            assertNotNull(emailColumn);
            assertTrue(emailColumn.unique());
            assertFalse(emailColumn.nullable());

            // Check password field annotations
            Column passwordColumn = User.class.getDeclaredField("password").getAnnotation(Column.class);
            assertNotNull(passwordColumn);
            assertFalse(passwordColumn.nullable());

            // Check phones relationship
            assertNotNull(User.class.getDeclaredField("phones").getAnnotation(OneToMany.class));
            assertNotNull(User.class.getDeclaredField("phones").getAnnotation(JoinColumn.class));

        } catch (NoSuchFieldException e) {
            fail("Field not found: " + e.getMessage());
        }
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        User user1 = User.builder().id(id).email("test@example.com").build();
        User user2 = User.builder().id(id).email("test@example.com").build();

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testNotEqualsWithDifferentIds() {
        User user1 = User.builder().id(UUID.randomUUID()).build();
        User user2 = User.builder().id(UUID.randomUUID()).build();

        assertNotEquals(user1, user2);
    }

    @Test
    void testPhoneRelationship() {
        Phone phone = new Phone();
        User user = User.builder()
                .phones(List.of(phone))
                .build();

        assertEquals(1, user.getPhones().size());
        assertEquals(phone, user.getPhones().get(0));
    }

    @Test
    void testActiveFlag() {
        User activeUser = User.builder().isActive(true).build();
        User inactiveUser = User.builder().isActive(false).build();

        assertTrue(activeUser.isActive());
        assertFalse(inactiveUser.isActive());
    }

    @Test
    void testTimestampFields() {
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .created(now)
                .lastLogin(now.plusDays(1))
                .build();

        assertEquals(now, user.getCreated());
        assertEquals(now.plusDays(1), user.getLastLogin());
    }

    @Test
    void testToString() {
        User user = User.builder()
                .name("John Doe")
                .email("john@example.com")
                .build();

        String userString = user.toString();
        assertTrue(userString.contains("User"));
        assertTrue(userString.contains("John Doe"));
        assertTrue(userString.contains("john@example.com"));
    }
}
