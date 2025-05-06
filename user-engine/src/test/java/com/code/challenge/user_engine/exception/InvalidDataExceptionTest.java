package com.code.challenge.user_engine.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InvalidDataExceptionTest {

    @Test
    void exceptionCanBeThrownAndCaught() {
        try {
            throw new InvalidDataException("Test message");
        } catch (InvalidDataException e) {
            assertEquals("Test message", e.getMessage());
        }
    }

    @Test
    void exceptionIsInstanceOfRuntimeException() {
        InvalidDataException exception = new InvalidDataException("Test");
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructorSetsMessageCorrectly() {
        String message = "Invalid data provided";
        InvalidDataException exception = new InvalidDataException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void exceptionWithEmptyMessage() {
        InvalidDataException exception = new InvalidDataException("");
        assertEquals("", exception.getMessage());
    }

    @Test
    void exceptionWithNullMessage() {
        InvalidDataException exception = new InvalidDataException(null);
        assertNull(exception.getMessage());
    }

    @Test
    void exceptionStackTraceContainsClassName() {
        try {
            throw new InvalidDataException("Test");
        } catch (InvalidDataException e) {
            assertTrue(e.getStackTrace()[0].toString().contains("InvalidDataException"));
        }
    }

    @Test
    void exceptionCanBeUsedInThrowableContext() {
        Throwable exception = new InvalidDataException("Test");
        assertEquals("Test", exception.getMessage());
    }

    @Test
    void exceptionToStringContainsClassNameAndMessage() {
        String message = "Test message";
        InvalidDataException exception = new InvalidDataException(message);
        String toString = exception.toString();

        assertTrue(toString.contains("InvalidDataException"));
        assertTrue(toString.contains(message));
    }

    @Test
    void exceptionHasNoCauseByDefault() {
        InvalidDataException exception = new InvalidDataException("Test");
        assertNull(exception.getCause());
    }

    @Test
    void exceptionCanBeChainedWithCause() {
        Throwable cause = new RuntimeException("Root cause");
        InvalidDataException exception = new InvalidDataException("Test");
        exception.initCause(cause);

        assertEquals(cause, exception.getCause());
    }
}
