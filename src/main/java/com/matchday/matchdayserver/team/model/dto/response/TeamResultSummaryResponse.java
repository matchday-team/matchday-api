package com.matchday.matchdayserver.team.model.dto.response;

import java.util.List;

import com.matchday.matchdayserver.match.model.enums.MatchResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeamResultSummaryResponse {

    @Schema(description = "팀 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    private String teamName;

    @Schema(description = "팀 이미지 URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String teamImageUrl;

    @Schema(description = "출전 경기수", requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "0")
    private int totalMatchCount;

    @Schema(description = "최다 출전 선수 이름, 팀이 수행한 경기가 없을 경우 null 반환", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String mostPlayedPlayerName;

    @Schema(description = "승/패/무", requiredMode = Schema.RequiredMode.REQUIRED)
    private MatchResultSummary winLossDraw;

    @Schema(description = "최근 경기 결과 WIN, LOSE, DRAW", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<MatchResult> recentMatchResults;

    @Schema(description = "득/실 비율", requiredMode = Schema.RequiredMode.REQUIRED)
    private GoalRatio goalRatio;
}
