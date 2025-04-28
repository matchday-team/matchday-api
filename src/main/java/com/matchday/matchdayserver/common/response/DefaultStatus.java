package com.matchday.matchdayserver.common.response;

import org.springframework.http.HttpStatus;

public enum DefaultStatus implements StatusInterface {
    OK(HttpStatus.OK.value(), HttpStatus.OK.value(), "OK"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), 400, "Wrong Request"),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "Unknown Error"),
    ;

    private final int httpStatusCode;
    private final int customStatusCode;
    private String description;

    DefaultStatus(int httpStatusCode, int customStatusCode, String description) {
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
