package com.matchday.matchdayserver.match.model.dto.response;

import com.matchday.matchdayserver.match.model.enums.MatchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Schema(description = "매치 결과 정보 응답 객체")
public class MatchResultInfoResponse {

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
    @Schema(description = "상대팀명", example = "레알 마드리드")
    private String awayTeamName;

    @NotNull
    @Schema(description = "매치명", example = "프리미어리그 결승")
    private String matchTitle;

    @NotNull
    @Schema(description = "매치 결과", example = "WIN")
    private MatchResult matchResult;

    @Schema(description = "홈팀 스코어", example = "3")
    private int homeScore;

    @Schema(description = "상대팀 스코어", example = "1")
    private int awayScore;

    @NotNull
    @Schema(description = "경기 시간")
    private int matchTime;

    @NotNull
    @Schema(description = "출전 선수")
    private int playerCount;

    @NotNull
    @Schema(description = "매치 장소", example = "토트넘 스타디움")
    private String stadium;

    @Schema(description = "경기 일시")
    private LocalDate matchDate;

    @Builder
    public MatchResultInfoResponse(Long matchId,
        Long homeTeamId,
        String homeTeamName,
        Long awayTeamId,
        String awayTeamName,
        String matchTitle,
        MatchResult matchResult,
        int homeScore, int awayScore,
        int matchTime,
        int playerCount,
        String stadium,
        LocalDate matchDate) {
        this.matchId = matchId;
        this.homeTeamId = homeTeamId;
        this.homeTeamName = homeTeamName;
        this.awayTeamId = awayTeamId;
        this.awayTeamName = awayTeamName;
        this.matchTitle = matchTitle;
        this.matchResult = matchResult;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.matchTime = matchTime;
        this.playerCount = playerCount;
        this.stadium = stadium;
        this.matchDate = matchDate;
    }
}
