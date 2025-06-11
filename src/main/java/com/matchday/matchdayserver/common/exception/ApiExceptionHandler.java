package com.matchday.matchdayserver.common.exception;

import com.matchday.matchdayserver.common.response.ApiExceptionResponse;
import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.common.response.DefaultStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiExceptionResponse<ApiExceptionInterface>> handleApiException(Exception ex, ApiException apiException) {
        return ResponseEntity.status(apiException.getStatus().getHttpStatusCode())
                .body(ApiExceptionResponse.error(apiException.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiExceptionResponse<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessageList = ex.getFieldErrors()
            .stream()
            .map(
                objectError -> {
                    String format = "%s : { %s } 은 %s";
                    return String.format(format, objectError.getField(),
                        objectError.getRejectedValue(), objectError.getDefaultMessage());
                })
            .toList();

        return ApiExceptionResponse.error(DefaultStatus.BAD_REQUEST,
            errorMessageList); //400 에러 발생 후 각 검증 어노테이션에서 설정한 msg 출력
    }

    //PreAuthorize 실패했을때 예외처리 핸들링
    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiExceptionResponse<String> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        log.warn("권한 거부: 타입=[{}], 메시지=[{}]", ex.getClass().getName(), ex.getMessage());
        return ApiExceptionResponse.error(DefaultStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiExceptionResponse<String> handleAllExceptions(Exception ex) {
      log.error("예외 발생: 타입=[{}], 메시지=[{}]", ex.getClass().getName(), ex.getMessage(), ex);
      return ApiExceptionResponse.error(DefaultStatus.UNKNOWN_ERROR);
    }
}
