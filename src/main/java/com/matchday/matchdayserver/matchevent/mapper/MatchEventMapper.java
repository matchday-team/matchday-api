package com.matchday.matchdayserver.matchevent.mapper;

import com.matchday.matchdayserver.matchevent.model.dto.MatchEventRequest;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventResponse;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.user.model.entity.User;

import java.time.Duration;
import java.time.LocalDateTime;

public class MatchEventMapper {

  public static MatchEvent toEntity(MatchEventRequest request, Match match, User user) {
    return MatchEvent.builder()
        .eventType(request.getEventType())
        .description(request.getDescription())
        .match(match)
        .user(user)
        .build();
  }

  public static MatchEventResponse toResponse(MatchEvent matchEvent, Team team) {
    Match match = matchEvent.getMatch();
    User user = matchEvent.getUser();
    Long elapsedMinutes = calculateElapsedMinutes(match.getStartTime().atDate(match.getMatchDate()),
        matchEvent.getEventTime());

    return MatchEventResponse.builder()
        .id(matchEvent.getId())
        .elapsedMinutes(elapsedMinutes)
        .teamId(team.getId())
        .teamName(team.getName())
        .userId(user.getId())
        .userName(user.getName())
        .eventLog(matchEvent.getEventType().value)
        .build();
  }

  private static Long calculateElapsedMinutes(LocalDateTime matchStartTime,
      LocalDateTime eventTime) {
    long minutes = Duration.between(matchStartTime, eventTime).toMinutes();
    return minutes < 0 ? 0L : minutes; // 음수인 경우 0으로 처리
  }
}
