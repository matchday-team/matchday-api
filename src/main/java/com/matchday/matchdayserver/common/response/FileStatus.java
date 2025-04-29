package com.matchday.matchdayserver.common.response;

public enum FileStatus implements StatusInterface {
    INVALID_FILE_EXTENSION(400, 8001, "지원하지 않는 파일 확장자입니다."),
    NOTFOUND_FILE(404, 8002, "존재하지 않는 파일입니다."),
    EMPTY_FILE_EXTENSION(404, 8003, "파일 확장자가 비어있습니다."),
    S3_ACCESS_ERROR(500, 8004, "S3 접근 중 오류가 발생했습니다.");
  ;

    private final int httpStatusCode;
    private final int customStatusCode;
    private final String description;

    FileStatus(int httpStatusCode, int customStatusCode, String description) {
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
