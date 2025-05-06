package com.code.challenge.user_engine.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super("Unauthorized : " + message);
    }
}
