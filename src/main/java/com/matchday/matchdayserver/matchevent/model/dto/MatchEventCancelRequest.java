package com.matchday.matchdayserver.matchevent.model.dto;

import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "경기 이벤트 취소(삭제) 요청 DTO")
public class MatchEventCancelRequest {
  @Schema(description = "매치 유저 ID", example = "1", nullable = true)
  private Long matchUserId;

  @Schema(description = "매치 ID", example = "10", nullable = false)
  private Long matchId;

  @Schema(description = "팀 ID", example = "5", nullable = false)
  private Long teamId;

  @Schema(description = "이벤트 타입", example = "GOAL", nullable = false)
  private MatchEventType matchEventType;
}
