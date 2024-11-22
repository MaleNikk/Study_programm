package com.example.webfluxSecuriryExample.exeption;

public class AppAuthException extends AppSecurityException{
    public AppAuthException(String message, String errorCode) {
        super(message, errorCode);
    }
}
