package com.matchday.matchdayserver.match.model.dto.response;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.matchevent.model.dto.ScoreResponse;
import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;


@Getter
@Schema(description = "경기 점수 응답 객체")
public class MatchScoreResponse {

    @NotNull
    @Schema(description = "경기 ID", example = "1")
    private long matchId;

    @NotNull
    @Schema(description = "홈팀 ID", example = "1")
    private long homeTeamId;

    @NotNull
    @Schema(description = "상대팀 ID", example = "2")
    private long awayTeamId;

    @NotNull
    @Schema(description = "홈팀 점수")
    private ScoreResponse homeScore;

    @NotNull
    @Schema(description = "상대팀 점수")
    private ScoreResponse awayScore;

    @Builder
    public MatchScoreResponse(long matchId, long homeTeamId, long awayTeamId) {
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.matchId = matchId;
        this.homeScore = ScoreResponse.defaultScore();
        this.awayScore = ScoreResponse.defaultScore();
    }

    public void updateScore(Long teamId, MatchEventType eventType) {
        // 자책골인 경우
        if (eventType == MatchEventType.OWN_GOAL) {
            if (teamId.equals(this.homeTeamId)) {
                updateScore(awayScore, MatchEventType.GOAL); // homeTeam이 자책골 기록시 awayTeam의 골
            } else if (teamId.equals(this.awayTeamId)) {
                updateScore(homeScore, MatchEventType.GOAL); // awayTeam이 자책골 기록시 homeTeam의 골
            } else {
                throw new ApiException(MatchStatus.NOTFOUND_TEAM);
            }
            return;
        }
        if (teamId.equals(this.homeTeamId)) {
            updateScore(homeScore, eventType);
        } else if (teamId.equals(this.awayTeamId)) {
            updateScore(awayScore, eventType);
        } else {
            throw new ApiException(MatchStatus.NOTFOUND_TEAM);
        }
    }

    private void updateScore(ScoreResponse score, MatchEventType eventType) {
        switch (eventType) {
            case GOAL -> score.upGoalCount();
            case SHOT -> score.upShotCount();
            case VALID_SHOT -> score.upValidShotCount();
            case CORNER_KICK -> score.upCornerKickCount();
            case FOUL -> score.upFoulCount();
            case OFFSIDE -> score.upOffsideCount();
            case YELLOW_CARD -> score.upWarningCount();
            case RED_CARD -> score.upWarningCount();
            case OWN_GOAL -> score.upOwnGoalCount();
        }
    }
}
