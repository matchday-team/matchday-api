package com.matchday.matchdayserver.matchuser.model.dto;

import com.matchday.matchdayserver.matchuser.model.enums.MatchUserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "매치 참가자 생성 요청")
public class MatchUserCreateRequest {
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "팀 ID", example = "1")
    private Long teamId;

    @Schema(description = "매치에서의 역할", example = "START_PLAYER")
    private MatchUserRole role;

    @Schema(description = "매치에서의 포지션", example = "FW")
    private String matchPosition;

    @Schema(description = "매치에서의 그리드 위치", example = "A1")
    private String matchGrid;
}
