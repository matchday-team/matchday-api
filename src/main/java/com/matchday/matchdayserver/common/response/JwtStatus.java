package com.matchday.matchdayserver.common.response;

public enum JwtStatus implements StatusInterface {
    UNAUTHORIZED(401, 9000, "JWT 토큰 검증 실패"),
    INVALID_AUTHORIZATION_HEADER(401, 9001, "올바른 Bearer 형식이 아님, Bearer(공백) 총 7자"),
    EXPIRED_TOKEN(401, 9002, "JWT 토큰이 만료됨"),
    SIGNATURE_NOT_MATCH(401, 9003, "JWT 서명이 적절하지 않음"),
    MALFORMED_TOKEN(401, 9004, "JWT 형식이 잘못됨"),
    INVALID_TOKEN(401, 9005, "유효하지 않은 JWT 토큰"),
    EMPTY_TOKEN(401,9006,"토큰이 존재하지 않거나 비어 있음")

    ;

    private final int httpStatusCode;
    private final int customStatusCode;
    private String description;

    JwtStatus(int httpStatusCode, int customStatusCode, String description) {
        this.httpStatusCode = httpStatusCode;
        this.customStatusCode = customStatusCode;
        this.description = description;
    }

    public void setCustomDescription(String customDescription) {
        this.description = this.description + "\n" + customDescription;
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