package com.matchday.matchdayserver.common.response;

public enum JwtStatus implements StatusInterface {
    UNAUTHORIZED(401, 9000, "JWT 토큰 검증 실패"),
    INVALID_AUTHORIZATION_HEADER(401, 9001, "올바른 Bearer 형식이 아님, Bearer(공백) 총 7자"),
    EXPIRED_ACCESS_TOKEN(401, 9002, "JWT 엑세스 토큰이 만료됨"),
    EXPIRED_REFRESH_TOKEN(401, 9003, "JWT 리프레시 토큰이 만료됨"),
    SIGNATURE_NOT_MATCH(401, 9004, "JWT 서명이 적절하지 않음"),
    MALFORMED_TOKEN(401, 9005, "JWT 형식이 잘못됨"),
    INVALID_TOKEN(401, 9006, "유효하지 않은 JWT 토큰"),
    INVALID_TOKEN_TYPE(400,9007,"적절한 토큰 타입이 아닙니다(ACCESS,REFRESH)"),
    NOTFOUND_TOKEN_IN_COOKIE(404,9008,"쿠키에서 토큰값을 찾을 수 없습니다"),
    NOTFOUND_USER(404,9009,"토큰 페이로드 정보로 찾을 수 없는 사용자 입니다."),
    NOTFOUND_COOKIE(404,9008,"쿠키에 데이터가 없습니다"),
    NOTFOUND_TOKEN_IN_DB(404,9009,"저장된 리프레시 토큰이 아닙니다"),
    INVALID_REFRESH_TOKEN(400,9010,"현재 유저에 해당하는 리프레시 토큰이 아닙니다"),


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