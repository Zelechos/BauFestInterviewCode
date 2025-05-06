package com.code.challenge.user_engine.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PhoneDto Test Suite")
class PhoneDtoTest {

    @Test
    void testDefaultConstructor() {
        PhoneDto phone = new PhoneDto();

        assertEquals(0L, phone.getNumber());
        assertEquals(0, phone.getCityCode());
        assertNull(phone.getCountryCode());
    }

    @Test
    void testAllArgsConstructor() {
        PhoneDto phone = new PhoneDto(1234567890L, 11, "AR");

        assertEquals(1234567890L, phone.getNumber());
        assertEquals(11, phone.getCityCode());
        assertEquals("AR", phone.getCountryCode());
    }

    @Test
    void testBuilderComplete() {
        PhoneDto phone = PhoneDto.builder()
                .number(9876543210L)
                .cityCode(351)
                .countryCode("US")
                .build();

        assertEquals(9876543210L, phone.getNumber());
        assertEquals(351, phone.getCityCode());
        assertEquals("US", phone.getCountryCode());
    }

    @Test
    void testBuilderPartial() {
        PhoneDto phone = PhoneDto.builder()
                .number(5551234567L)
                .build();

        assertEquals(5551234567L, phone.getNumber());
        assertEquals(0, phone.getCityCode()); // default int value
        assertNull(phone.getCountryCode());
    }

    @Test
    void testNumberSetterAndGetter() {
        PhoneDto phone = new PhoneDto();
        phone.setNumber(9998887777L);

        assertEquals(9998887777L, phone.getNumber());
    }

    @Test
    void testCityCodeSetterAndGetter() {
        PhoneDto phone = new PhoneDto();
        phone.setCityCode(54);

        assertEquals(54, phone.getCityCode());
    }

    @Test
    void testCountryCodeSetterAndGetter() {
        PhoneDto phone = new PhoneDto();
        phone.setCountryCode("BR");

        assertEquals("BR", phone.getCountryCode());
    }

    @Test
    void testEqualsSameValues() {
        PhoneDto phone1 = new PhoneDto(1234567890L, 11, "AR");
        PhoneDto phone2 = new PhoneDto(1234567890L, 11, "AR");

        assertEquals(phone1, phone2);
    }

    @Test
    void testEqualsDifferentValues() {
        PhoneDto phone1 = new PhoneDto(1234567890L, 11, "AR");
        PhoneDto phone2 = new PhoneDto(9876543210L, 351, "US");

        assertNotEquals(phone1, phone2);
    }

    @Test
    void testHashCodeConsistency() {
        PhoneDto phone = new PhoneDto(1234567890L, 11, "AR");
        int initialHashCode = phone.hashCode();

        assertEquals(initialHashCode, phone.hashCode());
    }

    @Test
    void testHashCodeEquality() {
        PhoneDto phone1 = new PhoneDto(1234567890L, 11, "AR");
        PhoneDto phone2 = new PhoneDto(1234567890L, 11, "AR");

        assertEquals(phone1.hashCode(), phone2.hashCode());
    }

    @Test
    void testToStringContainsAllFields() {
        PhoneDto phone = new PhoneDto(1234567890L, 11, "AR");
        String str = phone.toString();

        assertTrue(str.contains("number=1234567890"));
        assertTrue(str.contains("cityCode=11"));
        assertTrue(str.contains("countryCode=AR"));
    }

    @Test
    void testCanEqualSameClass() {
        PhoneDto phone1 = new PhoneDto();
        PhoneDto phone2 = new PhoneDto();

        assertTrue(phone1.canEqual(phone2));
    }

    @Test
    void testCanEqualDifferentClass() {
        PhoneDto phone = new PhoneDto();

        assertFalse(phone.canEqual("NotAPhoneDto"));
    }

    @Test
    void testBuilderWithNullCountryCode() {
        PhoneDto phone = PhoneDto.builder()
                .number(1234567890L)
                .cityCode(11)
                .countryCode(null)
                .build();

        assertEquals(1234567890L, phone.getNumber());
        assertEquals(11, phone.getCityCode());
        assertNull(phone.getCountryCode());
    }
}
