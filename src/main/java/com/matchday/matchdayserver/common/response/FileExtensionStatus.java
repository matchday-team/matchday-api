package com.matchday.matchdayserver.common.response;

import org.springframework.http.HttpStatus;

public enum FileExtensionStatus implements StatusInterface {
    INVALID_FILE_EXTENSION(400, 8001, "지원하지 않는 파일 확장자입니다."),
    ;

    private final int httpStatusCode;
    private final int customStatusCode;
    private final String description;

    FileExtensionStatus(int httpStatusCode, int customStatusCode, String description) {
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
