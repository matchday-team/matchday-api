package com.matchday.matchdayserver.team.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "팀 정보 응답 객체")
public class TeamResponse {
    @NotNull
    @Schema(description = "팀 ID")
    private Long id;

    @NotNull
    @Schema(description = "팀 이름")
    private String name;

    @NotNull
    @Schema(description = "팀 컬러(상의 컬러)")
    private String teamColor;

    @NotNull
    @Schema(description = "팀 하의 컬러")
    private String bottomColor;

    @NotNull
    @Schema(description = "팀 스타킹 컬러")
    private String stockingColor;

    @Schema(description = "팀 이미지", nullable = true)
    private String teamImg;

    @Builder
    public TeamResponse(Long id, String name, String teamColor, String bottomColor, String stockingColor, String teamImg) {
        this.id = id;
        this.name = name;
        this.teamColor = teamColor;
        this.bottomColor = bottomColor;
        this.stockingColor = stockingColor;
        this.teamImg = teamImg;
    }
}