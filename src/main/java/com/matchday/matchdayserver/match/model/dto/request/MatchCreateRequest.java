package com.matchday.matchdayserver.match.model.dto.request;

import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.model.enums.MatchState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class MatchCreateRequest {
  @Schema(description = "경기명", example = "경기명")
  private String title;

  @Schema(description = "홈팀 id", example = "홈팀 id")
  private Long homeTeamId;

  @Schema(description = "상대팀 id", example = "상대팀 id")
  private Long awayTeamId;

  @Schema(description = "매치 타입(리그/대회/친선)", example = "매치 타입")
  private Match.MatchType matchType;

  @Schema(description = "경기장 주소", example = "경기장 주소")
  private String stadium;

  @Schema(description = "경기 일자", example = "2025-04-01")
  private LocalDate matchDate;

  @Schema(description = "경기 시작 시간", example = "14:00:00")
  private LocalTime startTime;

  @Schema(description = "경기 종료 시간", example = "16:00:00")
  private LocalTime endTime;

  @Schema(description = "주심", example = "주심 이름", nullable = true)
  private String mainRefereeName;

  @Schema(description = "부심1", example = "부심1 이름", nullable = true)
  private String assistantReferee1;

  @Schema(description = "부심2", example = "부심2 이름", nullable = true)
  private String assistantReferee2;

  @Schema(description = "대기심", example = "대기심 이름", nullable = true)
    private String fourthReferee;

    @Schema(description = "경기 상태 (SCHEDULED, ONGOING, FINISHED)", example = "SCHEDULED")
    private MatchState matchState;
}