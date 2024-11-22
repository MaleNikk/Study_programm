package com.example.jwt_security.web.handler;

import com.example.jwt_security.exception.AlreadyExistException;
import com.example.jwt_security.exception.EntityNotFoundException;
import com.example.jwt_security.exception.RefreshTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class AppWebExceptionHandler {

    @ExceptionHandler(value = RefreshTokenException.class)
    public ResponseEntity<AppErrorResponseBody> refreshTokenExceptionHandler(
            RefreshTokenException exception, WebRequest webRequest) {
        return buildResponse(HttpStatus.FORBIDDEN, exception, webRequest);
    }

    @ExceptionHandler(value = AlreadyExistException.class)
    public ResponseEntity<AppErrorResponseBody> alreadyExistsHandler(
            AlreadyExistException exception, WebRequest webRequest) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception, webRequest);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<AppErrorResponseBody> notFoundHandler(
            EntityNotFoundException exception, WebRequest webRequest) {
        return buildResponse(HttpStatus.NOT_FOUND, exception, webRequest);
    }

    private ResponseEntity<AppErrorResponseBody> buildResponse(
            HttpStatus httpStatus, Exception exception, WebRequest webRequest) {
        return ResponseEntity.status(httpStatus)
                .body(AppErrorResponseBody.builder()
                        .message(exception.getMessage())
                        .description(webRequest.getDescription(false))
                        .build());

    }
}
