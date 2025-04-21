package com.matchday.matchdayserver.common.response;

public enum TeamStatus implements StatusInterface {
    DUPLICATE_TEAMNAME(400,5001, "이미 존재하는 팀 이름"),
    NOTFOUND_TEAM(400,5002, "해당 팀이 존재하지 않습니다"),
    ALREADY_JOINED_USER(400,5003, "팀에 이미 유저가 소속되어있음"),
    REQUIRED_TEAM_COLOR(400,5004, "팀 컬러를 입력하세요");

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
