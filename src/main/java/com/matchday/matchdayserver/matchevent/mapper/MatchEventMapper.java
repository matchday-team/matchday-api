package com.matchday.matchdayserver.matchevent.mapper;

import com.matchday.matchdayserver.matchevent.model.dto.MatchEventRequest;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventResponse;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.user.model.entity.User;

import java.time.Duration;
import java.time.LocalDateTime;

public class MatchEventMapper {

    public static MatchEvent toEntity(MatchEventRequest request, Match match, MatchUser matchUser) {
        return MatchEvent.builder()
            .eventType(request.getEventType())
            .description(request.getDescription())
            .match(match)
            .matchUser(matchUser)
            .build();
    }

    public static MatchEventResponse toResponse(MatchEvent matchEvent) {
        Match match = matchEvent.getMatch();
        Long elapsedMinutes = calculateElapsedMinutes(
            match.getStartTime().atDate(match.getMatchDate()),
            matchEvent.getEventTime());
        User user = matchEvent.getMatchUser().getUser();
        Team team = matchEvent.getMatchUser().getTeam();

        return MatchEventResponse.builder()
            .id(matchEvent.getId())
            .elapsedMinutes(elapsedMinutes)
            .teamId(team.getId())
            .teamName(team.getName())
            .userId(user.getId())
            .userName(user.getName())
            .eventLog(matchEvent.getEventType().name())
            .build();
    }

    private static Long calculateElapsedMinutes(LocalDateTime matchStartTime,
        LocalDateTime eventTime) {
        long minutes = Duration.between(matchStartTime, eventTime).toMinutes();
        return minutes < 0 ? 0L : minutes; // 음수인 경우 0으로 처리
    }
}
