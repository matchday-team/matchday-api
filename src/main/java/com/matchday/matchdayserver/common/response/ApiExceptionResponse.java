package com.matchday.matchdayserver.common.response;

import lombok.Getter;

@Getter
public class ApiExceptionResponse<T> extends ApiResponse<T> {

    private int customStatusCode;

    public ApiExceptionResponse(
        StatusInterface status
    ) {
        super(
            status.getHttpStatusCode(),
            null,
            status.getDescription()
        );
        this.customStatusCode = status.getCustomStatusCode();
    }

    public ApiExceptionResponse(
        StatusInterface status,
        T data
    ) {
        super(
            status.getHttpStatusCode(),
            data,
            status.getDescription()
        );
        this.customStatusCode = status.getCustomStatusCode();
    }

    public static <T> ApiExceptionResponse<T> error(StatusInterface status) {
        return new ApiExceptionResponse<>(status);
    }

    public static <T> ApiExceptionResponse<T> error(StatusInterface status, T data) {
        return new ApiExceptionResponse<>(status, data);
    }
}
