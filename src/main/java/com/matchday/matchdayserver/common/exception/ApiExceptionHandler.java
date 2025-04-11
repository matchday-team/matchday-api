package com.matchday.matchdayserver.common.exception;

import com.matchday.matchdayserver.common.response.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ApiResponse<ApiExceptionInterface> handleApiException(Exception ex, ApiException apiException) {
        return ApiResponse.error(apiException.getStatus());
    }
}
