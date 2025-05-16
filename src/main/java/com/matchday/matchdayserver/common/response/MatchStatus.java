package com.matchday.matchdayserver.common.response;

public enum MatchStatus implements StatusInterface {
    INVALID_TITLE(400, 6001, "경기명을 입력하세요"),
    NOTFOUND_TEAM(400, 6002, "존재하지 않는 팀입니다"),
    SAME_TEAM_ERROR(400, 6003, "홈 팀과 어웨이 팀은 동일할 수 없습니다"),
    INVALID_STADIUM(400, 6004, "경기장을 입력하세요"),
    INVALID_DATE(400, 6005, "과거 날짜는 등록할 수 없습니다"),
    TIME_ORDER_INVALID(400, 6006, "시작 시간은 종료시간 보다 빨라야합니다"),
    NOTFOUND_MATCH(404, 6007, "존재하지 않는 매치 입니다"),
    NOT_PARTICIPATING_PLAYER(400, 6008, "경기에 참여중이지 않은 선수입니다"),
    UNAUTHORIZED_RECORD(403, 6009, "기록할 수 없는 유저입니다"),
    SOCKET_ERROR(500, 6010, "웹소켓 오류"),
    TEAM_NOT_PARTICIPATING(400, 6011, "해당 팀은 입력받은 경기의 홈팀도, 어웨이팀도 아닙니다."),
    MEMO_NOT_FOUND(404, 6012, "존재하지 않는 매모 입니다"),
    DIFFERENT_TEAM_EXCHANGE(400, 6013, "서로 다른 팀의 선수는 교체할 수 없습니다"),
    INVALID_HALF_TYPE(400, 6014,"잘못된 HALF 타입입니다"),
    INVALID_TIME_RANGE(400, 6015,"종료 시간은 시작 시간보다 늦어야합니다"),
    SECOND_HALF_TIME_ERROR(400, 6016,"후반 시작 시간은 전반 종료 시간 보다 늦어야합니다"),
    NOTFOUND_MATCH_EVENT(404, 6017, "존재하지 않는 경기 이벤트입니다"),
    INVALID_HALF_PERIOD(404,6018,"유효한 경기 기간은 1분~45분입니다")
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
