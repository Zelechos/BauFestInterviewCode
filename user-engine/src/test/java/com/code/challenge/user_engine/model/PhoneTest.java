package com.code.challenge.user_engine.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

class PhoneTest {

    @Test
    void testNoArgsConstructor() {
        Phone phone = new Phone();
        assertNotNull(phone);
        assertNull(phone.getId());
        assertEquals(0, phone.getNumber());
        assertEquals(0, phone.getCityCode());
        assertNull(phone.getCountryCode());
    }

    @Test
    void testAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        long number = 1234567890L;
        int cityCode = 11;
        String countryCode = "+54";

        Phone phone = new Phone(id, number, cityCode, countryCode);

        assertEquals(id, phone.getId());
        assertEquals(number, phone.getNumber());
        assertEquals(cityCode, phone.getCityCode());
        assertEquals(countryCode, phone.getCountryCode());
    }

    @Test
    void testBuilder() {
        UUID id = UUID.randomUUID();
        Phone phone = Phone.builder()
                .id(id)
                .number(9876543210L)
                .cityCode(351)
                .countryCode("+54")
                .build();

        assertEquals(id, phone.getId());
        assertEquals(9876543210L, phone.getNumber());
        assertEquals(351, phone.getCityCode());
        assertEquals("+54", phone.getCountryCode());
    }

    @Test
    void testGetterSetter() {
        Phone phone = new Phone();
        UUID id = UUID.randomUUID();

        phone.setId(id);
        phone.setNumber(5551234567L);
        phone.setCityCode(11);
        phone.setCountryCode("+1");

        assertEquals(id, phone.getId());
        assertEquals(5551234567L, phone.getNumber());
        assertEquals(11, phone.getCityCode());
        assertEquals("+1", phone.getCountryCode());
    }

    @Test
    void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        Phone phone1 = Phone.builder()
                .id(id)
                .number(1234567890L)
                .cityCode(11)
                .countryCode("+54")
                .build();

        Phone phone2 = Phone.builder()
                .id(id)
                .number(1234567890L)
                .cityCode(11)
                .countryCode("+54")
                .build();

        assertEquals(phone1, phone2);
        assertEquals(phone1.hashCode(), phone2.hashCode());
    }

    @Test
    void testNotEqualsWithDifferentIds() {
        Phone phone1 = Phone.builder().id(UUID.randomUUID()).build();
        Phone phone2 = Phone.builder().id(UUID.randomUUID()).build();

        assertNotEquals(phone1, phone2);
    }

    @Test
    void testToString() {
        Phone phone = Phone.builder()
                .number(9876543210L)
                .cityCode(351)
                .countryCode("+54")
                .build();

        String phoneString = phone.toString();
        assertTrue(phoneString.contains("9876543210"));
        assertTrue(phoneString.contains("351"));
        assertTrue(phoneString.contains("+54"));
    }

    @Test
    void testEntityAnnotation() {
        assertNotNull(Phone.class.getAnnotation(Entity.class));
    }

    @Test
    void testIdAnnotation() throws NoSuchFieldException {
        assertNotNull(Phone.class.getDeclaredField("id").getAnnotation(Id.class));
        assertNotNull(Phone.class.getDeclaredField("id").getAnnotation(GeneratedValue.class));
    }

    @Test
    void testLombokAnnotations() {
        // Verify Lombok annotations by checking generated methods
        Phone phone = new Phone();
        phone.setNumber(1234567890L);
        assertEquals(1234567890L, phone.getNumber());

        Phone builtPhone = Phone.builder().cityCode(11).build();
        assertEquals(11, builtPhone.getCityCode());
    }
}
