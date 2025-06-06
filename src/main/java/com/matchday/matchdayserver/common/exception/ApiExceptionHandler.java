package com.matchday.matchdayserver.common.exception;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.common.response.DefaultStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<ApiExceptionInterface>> handleApiException(Exception ex, ApiException apiException) {
        return ResponseEntity.status(apiException.getStatus().getHttpStatusCode())
                .body(ApiResponse.error(apiException.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessageList = ex.getFieldErrors()
            .stream()
            .map(
                objectError -> {
                    String format = "%s : { %s } 은 %s";
                    return String.format(format, objectError.getField(),
                        objectError.getRejectedValue(), objectError.getDefaultMessage());
                })
            .toList();

        return ApiResponse.error(DefaultStatus.BAD_REQUEST,
            errorMessageList); //400 에러 발생 후 각 검증 어노테이션에서 설정한 msg 출력
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<String> handleAllExceptions(Exception ex) {
      log.error("예외 발생: 타입=[{}], 메시지=[{}]", ex.getClass().getName(), ex.getMessage(), ex);
      return ApiResponse.error(DefaultStatus.UNKNOWN_ERROR);
    }
}
