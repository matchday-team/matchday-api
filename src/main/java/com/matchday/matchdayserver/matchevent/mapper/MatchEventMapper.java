package com.matchday.matchdayserver.matchevent.mapper;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.match.model.enums.HalfType;
import com.matchday.matchdayserver.match.model.enums.MatchState;
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

import static com.matchday.matchdayserver.match.model.enums.HalfType.FIRST_HALF;
import static com.matchday.matchdayserver.match.model.enums.HalfType.SECOND_HALF;

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
        LocalDateTime eventTime = matchEvent.getEventTime();
        HalfType halfType = determineHalfType(eventTime, match);//전반 후반 판단
        LocalDateTime matchStartTime = getMatchStartTime(match, halfType);//전후반 경기가 시작된 시간 판단

        Long elapsedMinutes = calculateElapsedMinutes(
            matchStartTime,
            matchEvent.getEventTime());
        User user = Optional.ofNullable(matchEvent.getMatchUser().getUser()).orElseGet(User::mock);
        Team team = matchEvent.getMatchUser().getTeam();

        return MatchEventResponse.builder()
            .id(matchEvent.getId())
            .elapsedMinutes(elapsedMinutes)
            .halfType(halfType.name())
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

    private static HalfType determineHalfType(LocalDateTime eventTime, Match match) {
        if (match.getFirstHalfStartTime() == null) {
            throw new ApiException(MatchStatus.INVALID_MATCH_TIME);
        }
        if (match.getSecondHalfStartTime() == null) {
            return FIRST_HALF;
        }
        LocalDateTime secondHalfStart = match.getSecondHalfStartTime().atDate(match.getMatchDate());
        return eventTime.isBefore(secondHalfStart) ? FIRST_HALF : SECOND_HALF;
    }

    private static LocalDateTime getMatchStartTime(Match match, HalfType halfType) {
        return switch (halfType) {
            case FIRST_HALF -> match.getFirstHalfStartTime().atDate(match.getMatchDate());
            case SECOND_HALF -> match.getSecondHalfStartTime().atDate(match.getMatchDate());
        };
    }
}
