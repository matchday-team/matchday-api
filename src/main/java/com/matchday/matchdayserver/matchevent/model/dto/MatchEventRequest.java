package com.matchday.matchdayserver.matchevent.model.dto;

import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "경기 이벤트 요청 DTO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchEventRequest {
  @Schema(description = "이벤트 유저", example = "1")
  private Long userId;

  @Schema(description = "이벤트 타입", example = "GOAL")
  private MatchEventType eventType;

  @Schema(description = "이벤트 설명", example = "좋은 골이었습니다!")
  private String description;
}
