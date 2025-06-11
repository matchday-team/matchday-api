package com.matchday.matchdayserver.match.model.dto.request;

import com.matchday.matchdayserver.match.model.entity.Match.MatchType;
import java.time.LocalDate;
import java.time.LocalTime;

public class MockMatchCreateRequest {

    public static MatchCreateRequest create(Long team1Id, Long team2Id) {
        return new MatchCreateRequest(
            "테스트 경기",
            team1Id, team2Id,
            MatchType.리그,
            "서울월드컵경기장",
            LocalDate.of(2025, 6, 15), // 미래 날짜로 변경
            LocalTime.of(14, 0),
            LocalTime.of(16, 0),
            45,
            45,
            "김주심",
            "이부심",
            "박부심",
            "정대기");
    }

}