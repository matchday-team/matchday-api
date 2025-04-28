package com.matchday.matchdayserver.matchevent.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.model.Message;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.common.response.TeamStatus;
import com.matchday.matchdayserver.common.response.UserStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.matchevent.mapper.MatchEventMapper;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventRequest;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventResponse;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.user.model.entity.User;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.matchuser.repository.MatchUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchEventSaveService {

  private final MatchUserRepository matchUserRepository;
  private final SimpMessagingTemplate messagingTemplate;
  private final MatchEventStrategy matchEventStrategy;

  public void saveMatchEvent(Long matchId, Message<MatchEventRequest> request) {
    validateAuthUser(matchId, request.getToken());

    MatchUser matchUser = matchUserRepository
        .findByMatchIdAndUserIdWithFetch(matchId, request.getData().getUserId())
        .orElseThrow(() -> new ApiException(MatchStatus.NOT_PARTICIPATING_PLAYER));

    User user = matchUser.getUser();
    Match match = matchUser.getMatch();

    List<MatchEventResponse> matchEventResponse = matchEventStrategy
        .generateMatchEventLog(request.getData(), match, user);
    for (MatchEventResponse response : matchEventResponse) {
      messagingTemplate.convertAndSend("/topic/match/" + matchId, response);
    }
  }

  private void validateAuthUser(Long matchId, String token) {
    Long authId = Long.parseLong(token);

    MatchUser authUser = matchUserRepository
        .findByMatchIdAndUserIdWithFetch(matchId, authId)
        .orElseThrow(() -> new ApiException(UserStatus.NOTFOUND_USER));

    if (!authUser.getMatch().getId().equals(matchId)) {
      throw new ApiException(MatchStatus.NOT_PARTICIPATING_PLAYER);
    }
  }
}
