package com.matchday.matchdayserver.matchevent.model.dto;

import com.matchday.matchdayserver.match.model.enums.HalfType;
import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "경기 이벤트 응답 DTO")
public class MatchEventResponse {

    @NotNull
    @Schema(description = "이벤트 ID", example = "1")
    private Long id;

    @NotNull
    @Schema(description = "경기 시작 후 경과 시간(분)", example = "23")
    private Long elapsedMinutes;

    @NotNull
    @Schema(description = "전반/후반 여부", example = "FIRST_HALF")
    private String halfType;

    @NotNull
    @Schema(description = "팀 ID", example = "1")
    private Long teamId;

    @NotNull
    @Schema(description = "팀 이름", example = "FC 서울")
    private String teamName;

    @NotNull
    @Schema(description = "선수 ID", example = "1")
    private Long userId;

    @NotNull
    @Schema(description = "선수 이름", example = "손흥민")
    private String userName;

    @Schema(description = "이벤트 로그", example = "손흥민 경고")
    private MatchEventType eventLog;
}
