package com.code.challenge.user_engine.exception;

import com.code.challenge.user_engine.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserAlreadyExistsException.class, InvalidDataException.class})
    public ResponseEntity<List<ErrorResponse>> handleBadRequestExceptions(RuntimeException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .build();

        return ResponseEntity.badRequest().body(List.of(error));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<List<ErrorResponse>> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of(error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<List<ErrorResponse>> handleGenericException(Exception ex) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail("Internal server error: " + ex.getMessage())
                .build();

        return ResponseEntity.internalServerError().body(List.of(error));
    }
}
