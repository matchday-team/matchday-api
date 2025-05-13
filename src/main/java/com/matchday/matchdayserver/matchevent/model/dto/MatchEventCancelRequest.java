package com.matchday.matchdayserver.matchevent.model.dto;

import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchEventCancelRequest {
  private Long matchUserId;
  private Long matchId;
  private Long teamId;
  private MatchEventType matchEventType;
}
