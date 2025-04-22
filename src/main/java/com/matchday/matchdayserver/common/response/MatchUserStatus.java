package com.matchday.matchdayserver.common.response;

public enum MatchUserStatus implements StatusInterface {
    NOTFOUND_MATCH(404, 7001, "존재하지 않는 매치입니다");

    private final int httpStatusCode;
    private final int customStatusCode;
    private final String description;

    MatchUserStatus(int httpStatusCode, int customStatusCode, String description) {
        this.httpStatusCode = httpStatusCode;
        this.customStatusCode = customStatusCode;
        this.description = description;
    }

    @Override
    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    @Override
    public int getCustomStatusCode() {
        return customStatusCode;
    }

    @Override
    public String getDescription() {
        return description;
    }
}