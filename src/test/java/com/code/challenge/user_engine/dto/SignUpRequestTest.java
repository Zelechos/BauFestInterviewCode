package com.code.challenge.user_engine.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SignUpRequest Test Suite")
class SignUpRequestTest {

    private final Validator validator;

    public SignUpRequestTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void testDefaultConstructor() {
        SignUpRequest request = new SignUpRequest();

        assertNull(request.getName());
        assertNull(request.getEmail());
        assertNull(request.getPassword());
        assertNull(request.getPhones());
    }

    @Test
    void testAllArgsConstructor() {
        List<PhoneDto> phones = Collections.singletonList(new PhoneDto(1234567890L, 11, "AR"));
        SignUpRequest request = new SignUpRequest("John Doe", "abcdefg@domain.com", "passW12rd", phones);

        assertEquals("John Doe", request.getName());
        assertEquals("abcdefg@domain.com", request.getEmail());
        assertEquals("passW12rd", request.getPassword());
        assertEquals(1, request.getPhones().size());
    }

    @Test
    void testBuilderComplete() {
        List<PhoneDto> phones = Arrays.asList(
                new PhoneDto(1234567890L, 11, "AR"),
                new PhoneDto(9876543210L, 351, "US")
        );

        SignUpRequest request = SignUpRequest.builder()
                .name("Jane Smith")
                .email("example@test.com")
                .password("validP12ss")
                .phones(phones)
                .build();

        assertEquals("Jane Smith", request.getName());
        assertEquals("example@test.com", request.getEmail());
        assertEquals("validP12ss", request.getPassword());
        assertEquals(2, request.getPhones().size());
    }

    @Test
    void testValidEmail() {
        SignUpRequest request = SignUpRequest.builder()
                .email("abcdefg@test.com")
                .password("validP12ss")
                .build();

        var violations = validator.validateProperty(request, "email");
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidEmailWrongUserLength() {
        SignUpRequest request = SignUpRequest.builder()
                .email("abcd@test.com")
                .password("validP12ss")
                .build();

        var violations = validator.validateProperty(request, "email");
        assertFalse(violations.isEmpty());
        assertEquals("El correo debe tener exactamente 7 letras en el usuario y un formato válido",
                violations.iterator().next().getMessage());
    }

    @Test
    void testValidPassword() {
        SignUpRequest request = SignUpRequest.builder()
                .email("abcdefg@test.com")
                .password("passW12rd")
                .build();

        var violations = validator.validateProperty(request, "password");
        assertTrue(violations.isEmpty());
    }

    @Test
    void testPasswordMissingCapitalLetter() {
        SignUpRequest request = SignUpRequest.builder()
                .email("abcdefg@test.com")
                .password("passw12rd")
                .build();

        var violations = validator.validateProperty(request, "password");
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPasswordWrongNumberCount() {
        SignUpRequest request = SignUpRequest.builder()
                .email("abcdefg@test.com")
                .password("passW1234")
                .build();

        var violations = validator.validateProperty(request, "password");
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPasswordTooShort() {
        SignUpRequest request = SignUpRequest.builder()
                .email("abcdefg@test.com")
                .password("passW1")
                .build();

        var violations = validator.validateProperty(request, "password");
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPasswordTooLong() {
        SignUpRequest request = SignUpRequest.builder()
                .email("abcdefg@test.com")
                .password("passW12rd12345")
                .build();

        var violations = validator.validateProperty(request, "password");
        assertFalse(violations.isEmpty());
    }

    @Test
    void testEqualsAndHashCodeSameValues() {
        List<PhoneDto> phones = Collections.singletonList(new PhoneDto(1234567890L, 11, "AR"));
        SignUpRequest request1 = new SignUpRequest("John", "abcdefg@test.com", "passW12rd", phones);
        SignUpRequest request2 = new SignUpRequest("John", "abcdefg@test.com", "passW12rd", phones);

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testEqualsDifferentValues() {
        SignUpRequest request1 = new SignUpRequest("John", "abcdefg@test.com", "passW12rd", null);
        SignUpRequest request2 = new SignUpRequest("Jane", "hijklmn@test.com", "passW34rd", null);

        assertNotEquals(request1, request2);
    }

    @Test
    void testToStringContainsAllFields() {
        SignUpRequest request = new SignUpRequest("John", "abcdefg@test.com", "passW12rd", null);
        String str = request.toString();

        assertTrue(str.contains("name=John"));
        assertTrue(str.contains("email=abcdefg@test.com"));
        assertTrue(str.contains("password=passW12rd"));
    }

    @Test
    void testCanEqualSameClass() {
        SignUpRequest request1 = new SignUpRequest();
        SignUpRequest request2 = new SignUpRequest();

        assertTrue(request1.canEqual(request2));
    }

    @Test
    void testBuilderWithNullPhones() {
        SignUpRequest request = SignUpRequest.builder()
                .name("John")
                .email("abcdefg@test.com")
                .password("passW12rd")
                .phones(null)
                .build();

        assertEquals("John", request.getName());
        assertEquals("abcdefg@test.com", request.getEmail());
        assertEquals("passW12rd", request.getPassword());
        assertNull(request.getPhones());
    }
}
