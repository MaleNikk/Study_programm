package com.fluxing.security.exception;

public class RefreshTokenException extends RuntimeException{

    public RefreshTokenException(String message) {
        super(message);
    }

    public RefreshTokenException(String message, String cause) {
        super(message.concat(" - ").concat(cause));
    }
}
