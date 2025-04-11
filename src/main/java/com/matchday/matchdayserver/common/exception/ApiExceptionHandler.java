package com.matchday.matchdayserver.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiExceptionInterface> handleApiException(Exception ex, ApiException apiException) {
        return ResponseEntity
                .status(apiException.getStatus().getHttpStatusCode())
                .body(apiException);
    }
}
