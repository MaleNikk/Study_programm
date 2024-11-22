package com.example.webfluxSecuriryExample.exeption;

public class AppSecurityException extends RuntimeException {

    protected String errorCode;

    public AppSecurityException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
