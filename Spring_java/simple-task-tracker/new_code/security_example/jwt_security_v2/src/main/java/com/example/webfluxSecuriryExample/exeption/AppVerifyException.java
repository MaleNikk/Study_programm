package com.example.webfluxSecuriryExample.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AppVerifyException extends AppSecurityException{
    public AppVerifyException(String message) {
        super(message, "VERIFY_EXCEPTION");
    }
}
