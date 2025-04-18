package com.matchday.matchdayserver.match.model.dto.request;

import com.matchday.matchdayserver.match.model.entity.Match;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class MatchCreateRequest {
    private String title;
    private Long homeTeamId;
    private Long awayTeamId;
    private Match.MatchType matchType;
    private String stadium;
    private LocalDate matchDate;
    private LocalTime startTime;
    private LocalTime endTime;

}
