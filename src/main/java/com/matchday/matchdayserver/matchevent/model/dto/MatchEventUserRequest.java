package com.matchday.matchdayserver.matchevent.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Schema(description = "경기 이벤트 유저 요청 DTO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchEventUserRequest extends MatchEventRequest {
    @NotNull
    @Schema(description = "이벤트 유저", example = "1")
    private Long matchUserId;
}
