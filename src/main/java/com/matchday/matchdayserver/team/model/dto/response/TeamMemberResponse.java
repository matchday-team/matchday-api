package com.matchday.matchdayserver.team.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@Schema(description = "팀 멤버 응답 객체")
public class TeamMemberResponse {

    @NotNull
    @Schema(description = "팀 멤버 ID")
    private Long id;

    @NotNull
    @Schema(description = "멤버 이름")
    private String name;

    @Schema(description = "선수 Number", nullable = true)
    private Integer number;

    @Schema(description = "고정 포지션", nullable = true)
    private String defaultPosition;

    @Schema(description = "현재 팀에서 활동 여부", nullable = true)
    private Boolean isActive; //현재 팀에서 활동 여부

    @Schema(description = "프로필 이미지", nullable = true)
    private String imageUrl; //프로필 이미지 url

    @Schema(description = "누적 출전수")
    private int totalAppearances;

    @Schema(description = "누적 득점수")
    private int totalGoals;

    @Schema(description = "누적 경고수")
    private int totalWarnings;

    @Builder
    public TeamMemberResponse(Long id, String name, Integer number, String defaultPosition, Boolean isActive,String imageUrl, int totalAppearances, int totalGoals, int totalWarnings) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.defaultPosition = defaultPosition;
        this.isActive = isActive;
        this.imageUrl = imageUrl;
        this.totalAppearances = totalAppearances;
        this.totalGoals = totalGoals;
        this.totalWarnings = totalWarnings;
    }
}
