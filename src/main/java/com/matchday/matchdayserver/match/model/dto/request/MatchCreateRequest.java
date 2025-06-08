package com.matchday.matchdayserver.match.model.dto.request;

import com.matchday.matchdayserver.match.model.entity.Match;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchCreateRequest {
    @NotNull
    @Schema(description = "경기명", example = "경기명")
    private String title;

    @NotNull
    @Schema(description = "홈팀 id", example = "홈팀 id")
    private Long homeTeamId;

    @NotNull
    @Schema(description = "상대팀 id", example = "상대팀 id")
    private Long awayTeamId;

    @NotNull
    @Schema(description = "매치 타입(리그/대회/친선)", example = "리그")
    private Match.MatchType matchType;

    @NotNull
    @Schema(description = "경기장 주소", example = "서울월드컵경기장")
    private String stadium;

    @NotNull
    @Schema(description = "경기 일자", example = "2025-04-01")
    private LocalDate matchDate;

    @NotNull
    @Schema(description = "경기 시작 시간", example = "14:00:00")
    private LocalTime plannedStartTime;

    @NotNull
    @Schema(description = "경기 종료 시간", example = "16:00:00")
    private LocalTime plannedEndTime;

    @NotNull
    @Schema(description = "전반 진행 시간 (분)", example = "45")
    private Integer firstHalfPeriod;

    @NotNull
    @Schema(description = "후반 진행 시간 (분)", example = "45")
    private Integer secondHalfPeriod;

    @Schema(description = "주심", example = "김주심", nullable = true)
    private String mainRefereeName;

    @Schema(description = "부심1", example = "이부심", nullable = true)
    private String assistantReferee1;

    @Schema(description = "부심2", example = "박부심", nullable = true)
    private String assistantReferee2;

    @Schema(description = "대기심", example = "정대기", nullable = true)
    private String fourthReferee;
}
