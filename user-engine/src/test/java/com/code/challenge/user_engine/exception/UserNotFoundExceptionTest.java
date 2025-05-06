package com.code.challenge.user_engine.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserNotFoundExceptionTest {

    @Test
    void constructorWithEmailCreatesCorrectMessage() {
        String email = "test@example.com";
        UserNotFoundException exception = new UserNotFoundException(email);
        assertEquals("User with email test@example.com not found", exception.getMessage());
    }

    @Test
    void emailFieldIsCorrectlySet() {
        String email = "user@domain.com";
        UserNotFoundException exception = new UserNotFoundException(email);
        assertEquals(email, exception.getEmail());
    }

    @Test
    void userIdFieldIsNullWhenUsingEmailConstructor() {
        UserNotFoundException exception = new UserNotFoundException("test@test.com");
        assertNull(exception.getUserId());
    }

    @Test
    void exceptionIsInstanceOfRuntimeException() {
        UserNotFoundException exception = new UserNotFoundException("test@example.com");
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void emptyEmailStillCreatesValidMessage() {
        UserNotFoundException exception = new UserNotFoundException("");
        assertEquals("User with email  not found", exception.getMessage());
        assertEquals("", exception.getEmail());
    }

    @Test
    void nullEmailHandledInMessage() {
        UserNotFoundException exception = new UserNotFoundException(null);
        assertEquals("User with email null not found", exception.getMessage());
        assertNull(exception.getEmail());
    }

    @Test
    void exceptionCanBeThrownAndCaughtWithCorrectMessage() {
        String email = "missing@user.com";
        try {
            throw new UserNotFoundException(email);
        } catch (UserNotFoundException e) {
            assertEquals("User with email missing@user.com not found", e.getMessage());
            assertEquals(email, e.getEmail());
        }
    }

    @Test
    void exceptionStackTraceContainsClassName() {
        try {
            throw new UserNotFoundException("stack@trace.com");
        } catch (UserNotFoundException e) {
            assertTrue(e.getStackTrace()[0].toString().contains("UserNotFoundException"));
        }
    }

    @Test
    void toStringContainsFormattedMessage() {
        String email = "to@string.com";
        UserNotFoundException exception = new UserNotFoundException(email);
        String toString = exception.toString();
        assertTrue(toString.contains("UserNotFoundException"));
        assertTrue(toString.contains("User with email to@string.com not found"));
    }

    @Test
    void exceptionHasNoCauseByDefault() {
        UserNotFoundException exception = new UserNotFoundException("no.cause@test.com");
        assertNull(exception.getCause());
    }
}
