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

        String halfTypeString;
        LocalDateTime matchStartTime;

        if (match.getMatchState() == MatchState.PLAY_FIRST_HALF) {
            if (match.getFirstHalfStartTime() == null) {
                throw new ApiException(MatchStatus.INVALID_MATCH_TIME);
            }
            matchStartTime = match.getFirstHalfStartTime().atDate(match.getMatchDate());
            halfTypeString = "FIRST_HALF";

        } else if (match.getMatchState() == MatchState.PLAY_SECOND_HALF) {
            if (match.getSecondHalfStartTime() == null) {
                throw new ApiException(MatchStatus.INVALID_MATCH_TIME);
            }
            matchStartTime = match.getSecondHalfStartTime().atDate(match.getMatchDate());
            halfTypeString = "SECOND_HALF";

        } else {
            throw new ApiException(MatchStatus.NOT_IN_PLAY_MATCH);
        }

        Long elapsedMinutes = calculateElapsedMinutes(
            matchStartTime,
            matchEvent.getEventTime());
        User user = Optional.ofNullable(matchEvent.getMatchUser().getUser()).orElseGet(User::mock);
        Team team = matchEvent.getMatchUser().getTeam();

        return MatchEventResponse.builder()
            .id(matchEvent.getId())
            .elapsedMinutes(elapsedMinutes)
            .halfType(halfTypeString)
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
