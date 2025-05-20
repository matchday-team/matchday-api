package com.matchday.matchdayserver.match.model.enums;

public enum MatchState {
    SCHEDULED,        // 경기 전
    PLAY_FIRST_HALF,  // 전반 진행 중
    HALF_TIME,        // 전반과 후반 사이 쉬는 시간
    PLAY_SECOND_HALF, // 후반 진행 중
    FINISHED          // 경기 종료
}