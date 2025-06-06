package com.matchday.matchdayserver.common.response;

public enum MatchUserStatus implements StatusInterface {
    NOTFOUND_MATCH(404, 7001, "존재하지 않는 매치입니다"),
    ALREADY_REGISTERED(400, 7002, "매치에 이미 등록된 유저입니다"),
    NOTFOUND_MATCHUSER(404, 7003, "존재하지 않는 매치 유저입니다"),
    ALREADY_OCCUPYED(404,7004,"해당 좌표에 이미 누군가 배치되어있습니다")
    ;

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