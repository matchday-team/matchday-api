package com.matchday.matchdayserver.common.response;

public enum TeamStatus implements StatusInterface {
    DUPLICATE_TEAMNAME(400, 5001, "이미 존재하는 팀 이름"),
    NOTFOUND_TEAM(404,5002, "존재하지 않는 팀입니다"),
    ALREADY_JOINED_USER(400,5003, "팀에 이미 유저가 소속되어있음"),
    INACTIVE_USER_TEAM(400, 5004, "현재 해당 팀에 활동 중이 아닌 사용자입니다")
  ;

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
