package com.code.challenge.user_engine.controller;

import com.code.challenge.user_engine.dto.ErrorResponse;
import com.code.challenge.user_engine.dto.SignUpRequest;
import com.code.challenge.user_engine.dto.UserResponse;
import com.code.challenge.user_engine.exception.InvalidDataException;
import com.code.challenge.user_engine.exception.InvalidTokenException;
import com.code.challenge.user_engine.exception.UserAlreadyExistsException;
import com.code.challenge.user_engine.exception.UserNotFoundException;
import com.code.challenge.user_engine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request) {
        try {
            UserResponse response = userService.signUp(request);
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonList(
                            ErrorResponse.builder()
                                    .timestamp(LocalDateTime.now())
                                    .code(HttpStatus.CONFLICT.value())
                                    .detail(e.getMessage())
                                    .build()));
        } catch (InvalidDataException e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonList(
                            ErrorResponse.builder()
                                    .timestamp(LocalDateTime.now())
                                    .code(HttpStatus.BAD_REQUEST.value())
                                    .detail(e.getMessage())
                                    .build()));
        }
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestHeader("Authorization") String token) {
        try {
            UserResponse response = userService.login(token.replace("Bearer ", ""));
            return ResponseEntity.ok(response);
        } catch (InvalidTokenException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonList(
                            ErrorResponse.builder()
                                    .timestamp(LocalDateTime.now())
                                    .code(HttpStatus.UNAUTHORIZED.value())
                                    .detail(e.getMessage())
                                    .build()));
        }
    }
}
