package com.matchday.matchdayserver.common.response;

public enum AnnotationStatus implements StatusInterface{
    BAD_REQUEST_ANNOTATION(400, 11001, "올바른 Annotation 사용법이 아님"),
    ;

    private final int httpStatusCode;
    private final int customStatusCode;
    private final String description;

    AnnotationStatus(int httpStatusCode, int customStatusCode, String description) {
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
