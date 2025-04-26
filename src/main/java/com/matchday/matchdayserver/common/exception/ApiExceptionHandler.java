package com.matchday.matchdayserver.common.exception;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.common.response.DefaultStatus;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ApiResponse<ApiExceptionInterface> handleApiException(Exception ex, ApiException apiException) {
        return ApiResponse.error(apiException.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        String errorMessage = fieldErrors.stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                .orElse("유효성 검사 실패");

        return ApiResponse.error(DefaultStatus.BAD_REQUEST, errorMessage); //400 에러 발생 후 각 검증 어노테이션에서 설정한 msg 출력
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<String> handleAllExceptions(Exception ex) {
      return ApiResponse.error(DefaultStatus.UNKNOWN_ERROR);
    }
}
