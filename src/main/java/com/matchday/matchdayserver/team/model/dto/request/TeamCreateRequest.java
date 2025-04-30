package com.matchday.matchdayserver.team.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamCreateRequest {
    @Schema(description = "팀 이름", example = "팀 이름", required = true)
    @NotBlank(message = "팀 명은 필수입니다.")
    private String name; //팀명

    @Schema(description = "팀 컬러(상의 컬러)", example = "#FFFFFF", required = true)
    @NotBlank(message = "팀 컬러(상의 컬러)는 필수입니다.")
    private String teamColor; //팀 컬러(상의 컬러)

    @Schema(description = "팀 하의 컬러", example = "#FFFFFF", required = true)
    @NotBlank(message = "팀 하의 컬러는 필수입니다.")
    private String bottomColor; //팀 하의 컬러

    @Schema(description = "팀 스타킹 컬러", example = "#FFFFFF", required = true)
    @NotBlank(message = "팀 스타킹 컬러는 필수입니다.")
    private String stockingColor; //팀 스타킹 컬러
}