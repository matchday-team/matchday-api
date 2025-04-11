package com.matchday.matchdayserver.common.exception;

import com.matchday.matchdayserver.common.response.StatusInterface;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException implements ApiExceptionInterface {
    private final StatusInterface status;
    private final String detail;

    public ApiException(StatusInterface statusCode) {
        super(statusCode.getDescription());
        this.status = statusCode;
        this.detail = statusCode.getDescription();
    }

    /**
     * 세부적인 에러 메시지를 전달할 수 있는 생성자
     */
    public ApiException(StatusInterface statusCode, String detail) {
        super(detail);
        this.status = statusCode;
        this.detail = detail;
    }

    public ApiException(StatusInterface statusCode, Throwable throwable) {
        super(throwable);
        this.status = statusCode;
        this.detail = statusCode.getDescription();
    }

    public ApiException(StatusInterface statusCode, Throwable throwable, String detail) {
        super(throwable);
        this.status = statusCode;
        this.detail = detail;
    }
}
