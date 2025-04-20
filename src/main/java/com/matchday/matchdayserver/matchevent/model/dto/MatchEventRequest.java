package com.matchday.matchdayserver.matchevent.model.dto;

import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "경기 이벤트 요청 DTO")
@AllArgsConstructor
public class MatchEventRequest {
  @Schema(description = "경기 ID", example = "1")
  private Long matchId;

  @Schema(description = "사용자 ID", example = "1")
  private Long userId;

  @Schema(description = "이벤트 타입", example = "GOAL")
  private MatchEventType eventType;

  @Schema(description = "이벤트 설명", example = "좋은 골이었습니다!")
  private String description;
}

