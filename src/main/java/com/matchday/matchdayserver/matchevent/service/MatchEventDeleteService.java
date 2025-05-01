package com.matchday.matchdayserver.matchevent.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventDeleteResponse;
import com.matchday.matchdayserver.matchevent.repository.MatchEventRepository;
import com.matchday.matchdayserver.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchEventDeleteService {

  private final MatchEventRepository matchEventRepository;
  private final SimpMessagingTemplate simpMessagingTemplate;
  private final MatchRepository matchRepository;

  @Transactional
  public Long deleteAllEvents(Long matchId){
    matchRepository.findById(matchId).orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH));
    matchEventRepository.deleteByMatchId(matchId);
    MatchEventDeleteResponse response= new MatchEventDeleteResponse(matchId);
    simpMessagingTemplate.convertAndSend("/topic/matchevents", response);
    return matchId;
  }
}
