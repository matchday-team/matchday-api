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

  public static String generateEventLog(String userName, MatchEventType eventType) {
    return switch (eventType) {
      case GOAL -> userName + " 득점";
      case ASSIST -> userName + " 어시스트";
      case SHOT -> userName + " 슛";
      case VALID_SHOT -> userName + " 유효슛";
      case FOUL -> userName + " 파울";
      case OFFSIDE -> userName + " 오프사이드";
      case SUB_IN -> userName + " 교체 투입";
      case SUB_OUT -> userName + " 교체 아웃";
      case YELLOW_CARD -> userName + " 경고";
      case RED_CARD -> userName + " 퇴장";
      case OWN_GOAL -> userName + " 자책골";
    };
  }
}
