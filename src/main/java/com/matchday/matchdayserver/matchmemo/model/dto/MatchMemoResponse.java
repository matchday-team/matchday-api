package com.matchday.matchdayserver.matchmemo.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchMemoResponse {
  private Long matchId;
  private Long teamId;
  private String memo;
}