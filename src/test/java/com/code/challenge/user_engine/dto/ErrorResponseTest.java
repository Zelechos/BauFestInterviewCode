package com.code.challenge.user_engine.dto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ErrorResponse Test Suite")
class ErrorResponseTest {

    @Test
    void testDefaultConstructor() {
        ErrorResponse errorResponse = new ErrorResponse();

        assertNull(errorResponse.getTimestamp());
        assertEquals(0, errorResponse.getCode());
        assertNull(errorResponse.getDetail());
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse errorResponse = new ErrorResponse(now, 404, "Not Found");

        assertEquals(now, errorResponse.getTimestamp());
        assertEquals(404, errorResponse.getCode());
        assertEquals("Not Found", errorResponse.getDetail());
    }

    @Test
    void testBuilderComplete() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(now)
                .code(500)
                .detail("Internal Server Error")
                .build();

        assertEquals(now, errorResponse.getTimestamp());
        assertEquals(500, errorResponse.getCode());
        assertEquals("Internal Server Error", errorResponse.getDetail());
    }

    @Test
    void testBuilderPartial() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(400)
                .build();

        assertNull(errorResponse.getTimestamp());
        assertEquals(400, errorResponse.getCode());
        assertNull(errorResponse.getDetail());
    }

    @Test
    void testSettersAndGettersTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(now);

        assertEquals(now, errorResponse.getTimestamp());
    }

    @Test
    void testSettersAndGettersCode() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(401);

        assertEquals(401, errorResponse.getCode());
    }

    @Test
    void testSettersAndGettersDetail() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setDetail("Unauthorized");

        assertEquals("Unauthorized", errorResponse.getDetail());
    }

    @Test
    void testEqualsAndHashCodeSameObjects() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse error1 = new ErrorResponse(now, 404, "Not Found");
        ErrorResponse error2 = new ErrorResponse(now, 404, "Not Found");

        assertEquals(error1, error2);
        assertEquals(error1.hashCode(), error2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeDifferentObjects() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse error1 = new ErrorResponse(now, 404, "Not Found");
        ErrorResponse error2 = new ErrorResponse(now, 500, "Server Error");

        assertNotEquals(error1, error2);
        assertNotEquals(error1.hashCode(), error2.hashCode());
    }

    @Test
    void testToStringContainsAllFields() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse errorResponse = new ErrorResponse(now, 403, "Forbidden");
        String toString = errorResponse.toString();

        assertTrue(toString.contains("timestamp=" + now));
        assertTrue(toString.contains("code=403"));
        assertTrue(toString.contains("detail=Forbidden"));
    }

    @Test
    void testBuilderWithNullValues() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(null)
                .code(0)
                .detail(null)
                .build();

        assertNull(errorResponse.getTimestamp());
        assertEquals(0, errorResponse.getCode());
        assertNull(errorResponse.getDetail());
    }

    @Test
    void testEqualsNullComparison() {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 400, "Bad Request");

        assertNotEquals(null, errorResponse);
    }

    @Test
    void testEqualsDifferentClass() {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), 400, "Bad Request");

        assertNotEquals("A String Object", errorResponse);
    }

    @Test
    void testCanEqualSameClass() {
        ErrorResponse error1 = new ErrorResponse();
        ErrorResponse error2 = new ErrorResponse();

        assertTrue(error1.canEqual(error2));
    }

    @Test
    void testCanEqualDifferentClass() {
        ErrorResponse errorResponse = new ErrorResponse();

        assertFalse(errorResponse.canEqual("A String Object"));
    }
}
