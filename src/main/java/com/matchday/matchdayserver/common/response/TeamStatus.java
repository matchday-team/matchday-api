package com.matchday.matchdayserver.common.response;

public enum TeamStatus implements StatusInterface {
    DUPLICATE_TEAMNAME(400,4001, "이미 존재하는 팀 이름");

    private final int httpStatusCode;
    private final int customStatusCode;
    private final String description;

    TeamStatus(int httpStatusCode, int customStatusCode, String description) {
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
