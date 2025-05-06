package com.code.challenge.user_engine.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InvalidTokenExceptionTest {

    @Test
    void constructorPrefixesMessageWithUnauthorized() {
        String originalMessage = "Token expired";
        InvalidTokenException exception = new InvalidTokenException(originalMessage);
        assertEquals("Unauthorized : Token expired", exception.getMessage());
    }

    @Test
    void exceptionIsInstanceOfRuntimeException() {
        InvalidTokenException exception = new InvalidTokenException("Test");
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void emptyMessageStillGetsPrefix() {
        InvalidTokenException exception = new InvalidTokenException("");
        assertEquals("Unauthorized : ", exception.getMessage());
    }

    @Test
    void nullMessageHandledCorrectly() {
        InvalidTokenException exception = new InvalidTokenException(null);
        assertEquals("Unauthorized : null", exception.getMessage());
    }

    @Test
    void exceptionCanBeThrownAndCaught() {
        try {
            throw new InvalidTokenException("Invalid signature");
        } catch (InvalidTokenException e) {
            assertEquals("Unauthorized : Invalid signature", e.getMessage());
        }
    }

    @Test
    void exceptionStackTraceContainsClassName() {
        try {
            throw new InvalidTokenException("Test");
        } catch (InvalidTokenException e) {
            assertTrue(e.getStackTrace()[0].toString().contains("InvalidTokenException"));
        }
    }

    @Test
    void toStringContainsPrefixedMessage() {
        InvalidTokenException exception = new InvalidTokenException("Missing token");
        String toString = exception.toString();
        assertTrue(toString.contains("InvalidTokenException"));
        assertTrue(toString.contains("Unauthorized : Missing token"));
    }

    @Test
    void exceptionHasNoCauseByDefault() {
        InvalidTokenException exception = new InvalidTokenException("Test");
        assertNull(exception.getCause());
    }

    @Test
    void exceptionCanBeChainedWithCause() {
        Throwable cause = new RuntimeException("Root cause");
        InvalidTokenException exception = new InvalidTokenException("Test");
        exception.initCause(cause);

        assertEquals(cause, exception.getCause());
        assertEquals("Unauthorized : Test", exception.getMessage());
    }

    @Test
    void messageFormatIsConsistent() {
        String message1 = "Expired";
        String message2 = "Malformed";

        InvalidTokenException ex1 = new InvalidTokenException(message1);
        InvalidTokenException ex2 = new InvalidTokenException(message2);

        assertTrue(ex1.getMessage().startsWith("Unauthorized : "));
        assertTrue(ex2.getMessage().startsWith("Unauthorized : "));
        assertEquals("Expired", ex1.getMessage().substring(15));
        assertEquals("Malformed", ex2.getMessage().substring(15));
    }
}
