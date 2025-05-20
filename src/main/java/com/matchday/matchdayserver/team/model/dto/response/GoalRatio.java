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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalRatio {
    @Schema(description = "득점 수", requiredMode = Schema.RequiredMode.REQUIRED)
    private int goalsScored;

    @Schema(description = "실점 수", requiredMode = Schema.RequiredMode.REQUIRED)
    private int goalsConceded;
}
