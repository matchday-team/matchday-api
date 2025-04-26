package com.matchday.matchdayserver.match.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.match.model.dto.response.MatchInfoResponse;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.model.mapper.MatchMapper;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchService {
  private final MatchRepository matchRepository;

  public MatchInfoResponse getMatchInfo(Long matchId) {
    Match match = matchRepository.findById(matchId)
        .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH));
    return MatchMapper.toResponse(match);
  }

}
