package com.matchday.matchdayserver.matchevent.mapper;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventRequest;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventResponse;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.user.model.entity.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

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
        //만약 second_half_start_time 이 null 값이라면 아직 전반전인 것임
        LocalDateTime matchStartTime;
        if (match.getSecondHalfStartTime() == null) {
            if (match.getFirstHalfStartTime() == null || match.getMatchDate() == null) {
                throw new ApiException(MatchStatus.INVALID_MATCH_TIME);
            }
            matchStartTime = match.getFirstHalfStartTime().atDate(match.getMatchDate());//핵심
        } else {
            if (match.getMatchDate() == null) {
                throw new ApiException(MatchStatus.INVALID_MATCH_TIME);
            }
            matchStartTime = match.getSecondHalfStartTime().atDate(match.getMatchDate());//핵심
        }

        Long elapsedMinutes = calculateElapsedMinutes(
            matchStartTime,
            matchEvent.getEventTime());
        User user = Optional.ofNullable(matchEvent.getMatchUser().getUser()).orElseGet(User::mock);
        Team team = matchEvent.getMatchUser().getTeam();

        return MatchEventResponse.builder()
            .id(matchEvent.getId())
            .elapsedMinutes(elapsedMinutes)
            .teamId(team.getId())
            .teamName(team.getName())
            .userId(user.getId())
            .userName(user.getName())
            .eventLog(matchEvent.getEventType())
            .build();
    }

    private static Long calculateElapsedMinutes(LocalDateTime matchStartTime,
        LocalDateTime eventTime) {
        long minutes = Duration.between(matchStartTime, eventTime).toMinutes();
        return minutes < 0 ? 0L : minutes; // 음수인 경우 0으로 처리
    }
}
