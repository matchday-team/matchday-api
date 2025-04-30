package com.matchday.matchdayserver.match.model.mapper;

import com.matchday.matchdayserver.match.model.dto.response.MatchInfoResponse;
import com.matchday.matchdayserver.match.model.entity.Match;

public class MatchMapper {
  public static MatchInfoResponse toResponse(Match match) {
    return MatchInfoResponse.builder()
        .id(match.getId())
        .stadium(match.getStadium())
        .matchDate(match.getMatchDate())
        .startTime(match.getStartTime())
        .endTime(match.getEndTime())
        .mainRefereeName(match.getMainRefereeName())
        .assistantReferee1(match.getAssistantReferee1())
        .assistantReferee2(match.getAssistantReferee2())
        .fourthReferee(match.getFourthReferee())
        .firstHalfStartTime(match.getFirstHalfStartTime())
        .secondHalfStartTime(match.getSecondHalfStartTime())
        .homeTeamId(match.getHomeTeam().getId()) // 추가
        .awayTeamId(match.getAwayTeam().getId()) // 추가
        .build();
  }
}