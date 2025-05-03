package com.code.challenge.user_engine.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final String email;
    private final UUID userId;

    public UserNotFoundException(String email) {
        super("User with email " + email + " not found");
        this.email = email;
        this.userId = null;
    }

    public UserNotFoundException(UUID userId) {
        super("User with ID " + userId + " not found");
        this.userId = userId;
        this.email = null;
    }
}
