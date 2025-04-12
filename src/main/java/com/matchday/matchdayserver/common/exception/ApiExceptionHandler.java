package com.matchday.matchdayserver.common.exception;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.common.response.DefaultStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<ApiExceptionInterface>> handleApiException(Exception ex, ApiException apiException) {
        return ResponseEntity
            .status(apiException.getStatus().getHttpStatusCode())
            .body(ApiResponse.error(apiException.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ApiExceptionInterface>> handleException(Exception ex) {
        log.error("Internal server error", ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(DefaultStatus.SERVER_ERROR));
    }
}
