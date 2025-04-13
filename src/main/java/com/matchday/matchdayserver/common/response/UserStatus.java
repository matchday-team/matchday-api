package com.matchday.matchdayserver.common.response;

import java.io.Serializable;

public enum UserStatus implements StatusInterface {
    DUPLICATE_USERNAME(400,4001, "이미 존재하는 유저 이름");

    private final int httpStatusCode;
    private final int customStatusCode;
    private final String description;

    UserStatus(int httpStatusCode, int customStatusCode, String description) {
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
