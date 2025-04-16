package com.matchday.matchdayserver.user.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserJoinTeamRequest {

    @Schema(description = "가입할 팀의 ID", example = "1")
    private Long teamId;
    @Schema(description = "선수의 등번호", example = "7",nullable = true)
    private Integer number; // 등번호
    @Schema(description = "선수의 팀 내 포지션", example = "FW",nullable = true)
    private String defaultPosition; // 포지션
}
