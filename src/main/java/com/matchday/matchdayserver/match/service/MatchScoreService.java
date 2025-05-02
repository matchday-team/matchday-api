package com.matchday.matchdayserver.match.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.match.model.dto.response.MatchScoreResponse;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.matchevent.repository.MatchEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchScoreService {

  private final MatchEventRepository matchEventRepository;
  private final MatchRepository matchRepository;


  public MatchScoreResponse getMatchScore(Long matchId) {
    Match match = matchRepository.findById(matchId)
        .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH));
    MatchScoreResponse matchScoreResponse = MatchScoreResponse.builder()
        .homeTeamId(match.getHomeTeam().getId())
        .awayTeamId(match.getAwayTeam().getId())
        .matchId(match.getId())
        .build();
    List<MatchEvent> matchEvents = matchEventRepository.findByMatchId(matchId);

    for(MatchEvent matchEvent : matchEvents) {
      long teamId = matchEvent.getMatchUser().getTeam().getId();
      matchScoreResponse.updateScore(teamId, matchEvent.getEventType());
    }
    return matchScoreResponse;
  }
}
