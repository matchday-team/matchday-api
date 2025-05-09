package com.matchday.matchdayserver.match.model.dto.response;

import com.matchday.matchdayserver.match.model.enums.MatchState;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalTime;

@Getter
@Schema(description = "매치 리스트 응답 객체")
public class MatchListResponse {
    @NotNull
    @Schema(description = "매치 ID", example = "1")
    private Long matchId;

    @NotNull
    @Schema(description = "홈팀 ID", example = "1")
    private Long homeTeamId;

    @NotNull
    @Schema(description = "홈팀명", example = "토트넘")
    private String homeTeamName;

    @NotNull
    @Schema(description = "상대팀 ID", example = "2")
    private Long awayTeamId;

    @NotNull
    @Schema(description = "상대팀명" , example = "레알 마드리드")
    private String awayTeamName;

    @NotNull
    @Schema(description = "매치명", example = "프리미어리그 결승")
    private String matchTitle;

    @NotNull
    @Schema(description = "매치 시작 시간")
    private LocalTime matchStartTime;

    @NotNull
    @Schema(description = "매치 종료 시간")
    private LocalTime matchEndTime;

    @NotNull
    @Schema(description = "매치 장소", example = "토트넘 스타디움")
    private String stadium;

    @NotNull
    @Schema(description = "홈팀 스코어", example = "3")
    private int homeScore;

    @NotNull
    @Schema(description = "상대팀 스코어", example = "1")
    private int awayScore;

    @NotNull
    @Schema(description = "경기 진행 여부", example = "FINISHED")
     private MatchState matchState;  //경기 상태 (시작 전, 진행 중, 종료)

    @Builder
    public MatchListResponse(Long matchId, Long homeTeamId, String homeTeamName,
        Long awayTeamId, String awayTeamName, String matchTitle,
        LocalTime matchStartTime, LocalTime matchEndTime, String stadium,
        int homeScore, int awayScore,
        MatchState matchState) {
        this.matchId = matchId;
        this.homeTeamId = homeTeamId;
        this.homeTeamName = homeTeamName;
        this.awayTeamId = awayTeamId;
        this.awayTeamName = awayTeamName;
        this.matchTitle = matchTitle;
        this.matchStartTime = matchStartTime;
        this.matchEndTime = matchEndTime;
        this.stadium = stadium;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchState = matchState;
    }
}
