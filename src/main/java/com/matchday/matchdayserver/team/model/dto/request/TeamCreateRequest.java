package com.matchday.matchdayserver.team.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamCreateRequest {
    @Schema(description = "팀 이름", example = "팀 이름", required = true)
    private String name;//팀명
    @Schema(description = "팀 컬러(Hex code)", example = "#FFFFFF", required = true)
    private String teamColor; //팀 컬러
}