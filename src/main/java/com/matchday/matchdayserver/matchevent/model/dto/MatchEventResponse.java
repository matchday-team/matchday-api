package com.matchday.matchdayserver.matchevent.model.dto;

import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "경기 이벤트 응답 DTO")
public class MatchEventResponse {

    @Schema(description = "이벤트 ID", example = "1")
    private Long id;

    @Schema(description = "경기 시작 후 경과 시간(분)", example = "23")
    private Long elapsedMinutes;

    @Schema(description = "팀 ID", example = "1")
    private Long teamId;

    @Schema(description = "팀 이름", example = "FC 서울")
    private String teamName;

    @Schema(description = "선수 ID", example = "1")
    private Long userId;

    @Schema(description = "선수 이름", example = "손흥민")
    private String userName;

    @Schema(description = "이벤트 로그", example = "손흥민 경고")
    private String eventLog;
}
