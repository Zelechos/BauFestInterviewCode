package com.code.challenge.user_engine.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import com.code.challenge.user_engine.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleInvalidDataException_returnsBadRequest() {
        // Arrange
        InvalidDataException ex = new InvalidDataException("Invalid data provided");

        // Act
        ResponseEntity<List<ErrorResponse>> response = exceptionHandler.handleBadRequestExceptions(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid data provided", response.getBody().get(0).getDetail());
    }

    @Test
    void handleGenericException_returnsInternalServerError() {
        // Arrange
        Exception ex = new Exception("Unexpected error");

        // Act
        ResponseEntity<List<ErrorResponse>> response = exceptionHandler.handleGenericException(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getDetail().contains("Unexpected error"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().get(0).getCode());
    }

    @Test
    void allErrorResponses_containTimestamp() {
        // Arrange
        Exception ex = new Exception("Test error");

        // Act
        ResponseEntity<List<ErrorResponse>> response = exceptionHandler.handleGenericException(ex);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime errorTime = response.getBody().get(0).getTimestamp();

        // Assert
        assertNotNull(errorTime);
        assertTrue(errorTime.isBefore(now.plusSeconds(1)) || errorTime.isEqual(now));
        assertTrue(errorTime.isAfter(now.minusSeconds(1)) || errorTime.isEqual(now));
    }

    @Test
    void handleBadRequestExceptions_nullMessage_returnsDefaultDetail() {
        // Arrange
        UserAlreadyExistsException ex = new UserAlreadyExistsException(null);

        // Act
        ResponseEntity<List<ErrorResponse>> response = exceptionHandler.handleBadRequestExceptions(ex);

        // Assert
        assertNotNull(response.getBody().get(0).getDetail());
    }

    @Test
    void handleUserNotFoundException_responseContainsCorrectStatusCode() {
        // Arrange
        UserNotFoundException ex = new UserNotFoundException("Not found");

        // Act
        ResponseEntity<List<ErrorResponse>> response = exceptionHandler.handleUserNotFoundException(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().get(0).getCode());
    }

    @Test
    void handleBadRequestExceptions_differentExceptionTypesSameHandler() {
        // Arrange
        UserAlreadyExistsException ex1 = new UserAlreadyExistsException("Exists");
        InvalidDataException ex2 = new InvalidDataException("Invalid");

        // Act
        ResponseEntity<List<ErrorResponse>> response1 = exceptionHandler.handleBadRequestExceptions(ex1);
        ResponseEntity<List<ErrorResponse>> response2 = exceptionHandler.handleBadRequestExceptions(ex2);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    }

    @Test
    void allErrorResponses_haveNonNullFields() {
        // Arrange
        Exception ex = new Exception("Test");

        // Act
        ResponseEntity<List<ErrorResponse>> response = exceptionHandler.handleGenericException(ex);
        ErrorResponse error = response.getBody().get(0);

        // Assert
        assertNotNull(error.getTimestamp());
        assertNotNull(error.getCode());
        assertNotNull(error.getDetail());
    }
}
