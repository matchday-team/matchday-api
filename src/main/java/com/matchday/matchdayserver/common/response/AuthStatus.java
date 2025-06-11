package com.matchday.matchdayserver.common.response;

public enum AuthStatus implements StatusInterface {
    UNAUTHORIZED(401, 10001, "인증되지 않은 사용자, Authentication 객체가 없음"),
    FORBIDDEN(401, 10002, "권한이 부족합니다."),
    INVALID_REDIRECT_URI(400,10003,"승인된 redirection uri 가 아닙니다")
    ;

    private final int httpStatusCode;
    private final int customStatusCode;
    private final String description;

    AuthStatus(int httpStatusCode, int customStatusCode, String description) {
        this.httpStatusCode = httpStatusCode;
        this.customStatusCode = customStatusCode;
        this.description = description;
    }

    @Override
    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    @Override
    public int getCustomStatusCode() {
        return this.customStatusCode;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
