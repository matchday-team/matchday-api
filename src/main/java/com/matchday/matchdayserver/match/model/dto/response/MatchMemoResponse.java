package com.matchday.matchdayserver.match.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MatchMemoResponse {
  private String memo;
}