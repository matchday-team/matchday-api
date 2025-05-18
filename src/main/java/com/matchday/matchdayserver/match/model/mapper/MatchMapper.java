package com.matchday.matchdayserver.match.model.mapper;

import com.matchday.matchdayserver.match.model.dto.response.MatchInfoResponse;
import com.matchday.matchdayserver.match.model.dto.response.MatchListResponse;
import com.matchday.matchdayserver.match.model.dto.response.MatchResultInfoResponse;
import com.matchday.matchdayserver.match.model.dto.response.MatchScoreResponse;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.model.enums.MatchResult;

public class MatchMapper {
    // Match → MatchInfoResponse 변환
    public static MatchInfoResponse toResponse(Match match) {
        return MatchInfoResponse.builder()
            .id(match.getId())
            .title(match.getTitle())
            .stadium(match.getStadium())
            .matchDate(match.getMatchDate())
            .plannedStartTime(match.getPlannedStartTime())
            .plannedEndTime(match.getPlannedEndTime())
            .firstHalfPeriod(match.getFirstHalfPeriod())      // 추가
            .secondHalfPeriod(match.getSecondHalfPeriod())    // 추가
            .mainRefereeName(match.getMainRefereeName())
            .assistantReferee1(match.getAssistantReferee1())
            .assistantReferee2(match.getAssistantReferee2())
            .fourthReferee(match.getFourthReferee())
            .firstHalfStartTime(match.getFirstHalfStartTime())
            .firstHalfEndTime(match.getFirstHalfEndTime())
            .secondHalfStartTime(match.getSecondHalfStartTime())
            .secondHalfEndTime(match.getSecondHalfEndTime())
            .homeTeamId(match.getHomeTeam().getId())
            .awayTeamId(match.getAwayTeam().getId())
            .build();
    }

    public static MatchListResponse toMatchListResponse(Match match, MatchScoreResponse scoreResponse) {
        return MatchListResponse.builder()
            .matchId(match.getId())
            .homeTeamId(match.getHomeTeam().getId())
            .homeTeamName(match.getHomeTeam().getName())
            .awayTeamId(match.getAwayTeam().getId())
            .awayTeamName(match.getAwayTeam().getName())
            .matchTitle(match.getTitle())
            .plannedStartTime(match.getPlannedStartTime())
            .plannedEndTime(match.getPlannedEndTime())
            .stadium(match.getStadium())
            .homeScore(scoreResponse.getHomeScore().getGoalCount())
            .awayScore(scoreResponse.getAwayScore().getGoalCount())
            .matchState(match.getMatchState())
            .build();
    }

    public static MatchResultInfoResponse toMatchResultInfoResponse(
        Match match,
        MatchResult matchResult,
        int matchTime,
        int playerCount, MatchScoreResponse scoreResponse
    ) {
        return MatchResultInfoResponse.builder()
            .matchId(match.getId())
            .homeTeamId(match.getHomeTeam().getId())
            .homeTeamName(match.getHomeTeam().getName())
            .awayTeamId(match.getAwayTeam().getId())
            .awayTeamName(match.getAwayTeam().getName())
            .matchTitle(match.getTitle())
            .matchResult(matchResult)
            .homeScore(scoreResponse.getHomeScore().getGoalCount())
            .awayScore(scoreResponse.getAwayScore().getGoalCount())
            .matchTime(matchTime)
            .playerCount(playerCount)
            .stadium(match.getStadium())
            .matchDate(match.getMatchDate())
            .build();
    }
}
