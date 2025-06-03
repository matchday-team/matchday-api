package com.matchday.matchdayserver.team.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class GoalRatio {
    @Schema(description = "득점 수", requiredMode = Schema.RequiredMode.REQUIRED)
    private int goalsScored;

    @Schema(description = "실점 수", requiredMode = Schema.RequiredMode.REQUIRED)
    private int goalsConceded;

    public GoalRatio() {
        this.goalsScored = 0;
        this.goalsConceded = 0;
    }

    public void incrementGoalsScored(int goalsScored) {
        this.goalsScored += goalsScored;
    }

    public void incrementGoalsConceded(int goalsConceded) {
        this.goalsConceded += goalsConceded;
    }
}
