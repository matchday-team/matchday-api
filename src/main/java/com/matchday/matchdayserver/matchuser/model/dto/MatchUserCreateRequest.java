package com.matchday.matchdayserver.matchuser.model.dto;

import com.matchday.matchdayserver.matchuser.model.enums.MatchUserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "매치 참가자 생성 요청")
public class MatchUserCreateRequest {
    @NotNull
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "팀 ID", example = "1", nullable = true)
    private Long teamId;

    @NotNull
    @Schema(description = "매치에서의 역할", example = "START_PLAYER")
    private MatchUserRole role;

    @Schema(description = "매치에서의 포지션", example = "FW", nullable = true)
    private String matchPosition;

    @Min(value = 0, message = "0 이상의 값이어야 합니다.")
    @Max(value = 29, message = "29 이하의 값이어야 합니다.")
    @Schema(description = "매치에서의 선수 그리드 좌표(0~29)", example = "1", nullable = true)
    private Integer matchGrid;

}
