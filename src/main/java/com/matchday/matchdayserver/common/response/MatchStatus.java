package com.matchday.matchdayserver.common.response;

public enum MatchStatus implements StatusInterface {
    INVALID_TITLE(400, 6001, "경기명을 입력하세요"),
    NOTFOUND_TEAM(400, 6002, "존재하지 않는 팀입니다"),
    SAME_TEAM_ERROR(400, 6003,"홈 팀과 어웨이 팀은 동일할 수 없습니다"),
    INVALID_STADIUM(400, 6004, "경기장을 입력하세요"),
    INVALID_DATE(400, 6005, "과거 날짜는 등록할 수 없습니다"),
    INVALID_TIME(400, 6006, "시작시간이 종료시간 보다 늦을 수 없습니다"),
    NOTFOUND_MATCH(404, 6007, "존재하지 않는 매치 입니다"),
    NOT_PARTICIPATING_PLAYER(400, 6008, "경기에 참여중이지 않은 선수입니다"),
    ;


    private final int httpStatusCode;
    private final int customStatusCode;
    private final String description;

    MatchStatus(int httpStatusCode, int customStatusCode, String description) {
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

