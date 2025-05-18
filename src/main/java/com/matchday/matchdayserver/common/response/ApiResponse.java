package com.matchday.matchdayserver.common.response;

import org.springframework.http.HttpStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {
    private int status;
    private T data;
    private String message;

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(HttpStatus.OK.value(), null, DefaultStatus.OK.getDescription());
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), data, DefaultStatus.OK.getDescription());
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), data, DefaultStatus.OK.getDescription());
    }


    public static <T> ApiResponse<T> error(StatusInterface status) {
        return new ApiResponse<>(status.getCustomStatusCode(), null, status.getDescription());
    }

    public static <T> ApiResponse<T> error(StatusInterface status, T data) {
        return new ApiResponse<>(status.getCustomStatusCode(), data, status.getDescription());
    }
}
