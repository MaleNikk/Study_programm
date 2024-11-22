package com.fluxing.security.handler;

import com.fluxing.security.exception.AlreadyExistsException;
import com.fluxing.security.exception.EntityNotFoundException;
import com.fluxing.security.exception.RefreshTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class WebAppExceptionHandler {

    @ExceptionHandler(value = RefreshTokenException.class)
    public ResponseEntity<ErrorResponseBody> refreshTokenExceptionHandler(
            RefreshTokenException exception, WebRequest webRequest) {
        return buildResponse(HttpStatus.FORBIDDEN, exception, webRequest);
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseBody> alreadyExistsHandler(
            AlreadyExistsException exception, WebRequest webRequest) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception, webRequest);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> notFoundHandler(
            EntityNotFoundException exception, WebRequest webRequest) {
        return buildResponse(HttpStatus.NOT_FOUND, exception, webRequest);
    }

    private ResponseEntity<ErrorResponseBody> buildResponse(
            HttpStatus httpStatus,
            Exception exception,
            WebRequest webRequest
    ) {
        return ResponseEntity.status(httpStatus)
                .body(ErrorResponseBody
                        .builder()
                        .message(exception.getMessage())
                        .description(webRequest.getDescription(false))
                        .build());
    }

}
