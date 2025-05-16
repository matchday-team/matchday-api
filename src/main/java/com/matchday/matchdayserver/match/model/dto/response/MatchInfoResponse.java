package com.matchday.matchdayserver.match.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@Schema(description = "매치 정보 응답 객체")
public class MatchInfoResponse {
    @Schema(description = "id", example = "매치 id")
    private Long id;

    @NotNull
    @Schema(description = "홈팀 ID")
    private Long homeTeamId;

    @NotNull
    @Schema(description = "어웨이팀 ID")
    private Long awayTeamId;

    @NotNull
    @Schema(description = "장소", example = "경기 장소")
    private String stadium;

    @NotNull
    @Schema(description = "날짜")
    private LocalDate matchDate;

    @NotNull
    @Schema(description = "시작 예정 시간")
    private LocalTime plannedStartTime;

    @NotNull
    @Schema(description = "종료 예정 시간")
    private LocalTime plannedEndTime;

    @NotNull
    @Schema(description = "전반 진행 시간 (분)", example = "45")
    private Integer firstHalfPeriod;

    @NotNull
    @Schema(description = "후반 진행 시간 (분)", example = "45")
    private Integer secondHalfPeriod;

    @Schema(description = "주심", example = "주심", nullable = true)
    private String mainRefereeName;

    @Schema(description = "부심1", example = "부심1", nullable = true)
    private String assistantReferee1;

    @Schema(description = "부심2", example = "부심2", nullable = true)
    private String assistantReferee2;

    @Schema(description = "대기심", example = "대기심", nullable = true)
    private String fourthReferee;

    @Schema(description = "전반 시작 시간", nullable = true)
    private LocalTime firstHalfStartTime;

    @Schema(description = "전반 종료 시간", nullable = true)
    private LocalTime firstHalfEndTime;

    @Schema(description = "후반 시작 시간", nullable = true)
    private LocalTime secondHalfStartTime;

    @Schema(description = "후반 종료 시간", nullable = true)
    private LocalTime secondHalfEndTime;

    @Builder
    public MatchInfoResponse(Long id, String stadium, LocalDate matchDate, LocalTime plannedStartTime,
        LocalTime plannedEndTime, Integer firstHalfPeriod, Integer secondHalfPeriod,
        String mainRefereeName, String assistantReferee1, String assistantReferee2, String fourthReferee,
        LocalTime firstHalfStartTime, LocalTime firstHalfEndTime,
        LocalTime secondHalfStartTime, LocalTime secondHalfEndTime,
        Long homeTeamId, Long awayTeamId) {
        this.id = id;
        this.stadium = stadium;
        this.matchDate = matchDate;
        this.plannedStartTime = plannedStartTime;
        this.plannedEndTime = plannedEndTime;
        this.firstHalfPeriod = firstHalfPeriod;
        this.secondHalfPeriod = secondHalfPeriod;
        this.mainRefereeName = mainRefereeName;
        this.assistantReferee1 = assistantReferee1;
        this.assistantReferee2 = assistantReferee2;
        this.fourthReferee = fourthReferee;
        this.firstHalfStartTime = firstHalfStartTime;
        this.firstHalfEndTime = firstHalfEndTime;
        this.secondHalfStartTime = secondHalfStartTime;
        this.secondHalfEndTime = secondHalfEndTime;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
    }
}