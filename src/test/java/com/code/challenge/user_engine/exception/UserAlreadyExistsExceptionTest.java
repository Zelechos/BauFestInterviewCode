package com.code.challenge.user_engine.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserAlreadyExistsExceptionTest {

    @Test
    void constructorCreatesCorrectMessageWithEmail() {
        String email = "test@example.com";
        UserAlreadyExistsException exception = new UserAlreadyExistsException(email);
        assertEquals("User with email test@example.com already exists", exception.getMessage());
    }

    @Test
    void exceptionIsInstanceOfRuntimeException() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException("test@example.com");
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void emptyEmailStillCreatesValidMessage() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException("");
        assertEquals("User with email  already exists", exception.getMessage());
    }

    @Test
    void nullEmailHandledInMessage() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException(null);
        assertEquals("User with email null already exists", exception.getMessage());
    }

    @Test
    void exceptionCanBeThrownAndCaughtWithCorrectMessage() {
        String email = "user@domain.com";
        try {
            throw new UserAlreadyExistsException(email);
        } catch (UserAlreadyExistsException e) {
            assertEquals("User with email user@domain.com already exists", e.getMessage());
        }
    }

    @Test
    void exceptionStackTraceContainsClassName() {
        try {
            throw new UserAlreadyExistsException("test@test.com");
        } catch (UserAlreadyExistsException e) {
            assertTrue(e.getStackTrace()[0].toString().contains("UserAlreadyExistsException"));
        }
    }

    @Test
    void toStringContainsFormattedMessage() {
        String email = "admin@system.com";
        UserAlreadyExistsException exception = new UserAlreadyExistsException(email);
        String toString = exception.toString();
        assertTrue(toString.contains("UserAlreadyExistsException"));
        assertTrue(toString.contains("User with email admin@system.com already exists"));
    }

    @Test
    void exceptionHasNoCauseByDefault() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException("no.cause@test.com");
        assertNull(exception.getCause());
    }

    @Test
    void exceptionCanBeChainedWithCauseWhilePreservingMessage() {
        Throwable cause = new RuntimeException("Database constraint violation");
        String email = "chained@example.com";
        UserAlreadyExistsException exception = new UserAlreadyExistsException(email);
        exception.initCause(cause);

        assertEquals(cause, exception.getCause());
        assertEquals("User with email chained@example.com already exists", exception.getMessage());
    }

    @Test
    void messageFormatIsConsistentForDifferentEmails() {
        String email1 = "first@user.com";
        String email2 = "second@user.com";

        UserAlreadyExistsException ex1 = new UserAlreadyExistsException(email1);
        UserAlreadyExistsException ex2 = new UserAlreadyExistsException(email2);

        assertTrue(ex1.getMessage().startsWith("User with email "));
        assertTrue(ex1.getMessage().endsWith(" already exists"));
        assertTrue(ex2.getMessage().startsWith("User with email "));
        assertTrue(ex2.getMessage().endsWith(" already exists"));
        assertEquals(email1, ex1.getMessage().replace("User with email ", "").replace(" already exists", ""));
        assertEquals(email2, ex2.getMessage().replace("User with email ", "").replace(" already exists", ""));
    }
}
