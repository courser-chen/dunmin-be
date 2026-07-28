package com.dunmin.security.error;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
