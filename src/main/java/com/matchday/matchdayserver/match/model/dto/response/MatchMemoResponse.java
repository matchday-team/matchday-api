package com.matchday.matchdayserver.match.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MatchMemoResponse {
  private Long matchId;
  private Long teamId;
  private String memo;
}