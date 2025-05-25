package com.matchday.matchdayserver.matchevent.model.dto;

import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "경기 이벤트 취소 응답")
public class MatchEventCancelResponse {
    @NotNull
    @Schema(description = "이벤트 ID", example = "1")
    private Long id;

    @NotNull
    @Schema(description = "팀 ID", example = "2")
    private Long teamId;

    @Schema(description = "매치 유저 ID", example = "13")
    private Long matchUserId;

    @NotNull
    @Schema(description = "취소된 이벤트 로그", example = "골")
    private MatchEventType cancelEventLog;
}
