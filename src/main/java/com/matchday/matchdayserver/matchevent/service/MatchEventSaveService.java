package com.matchday.matchdayserver.matchevent.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.model.Message;
import com.matchday.matchdayserver.common.response.DefaultStatus;
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

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.matchday.matchdayserver.matchevent.repository.MatchEventRepository;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.matchuser.repository.MatchUserRepository;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchEventSaveService {

  private final MatchUserRepository matchUserRepository;
  private final MatchEventRepository matchEventRepository;
  private final TeamRepository teamRepository;
  private final SimpMessagingTemplate messagingTemplate;

  public void saveMatchEvent(Long matchId, Message<MatchEventRequest> request) {
    validateRequest(matchId, request);
    validateAuthUser(matchId, request.getToken());

    MatchUser matchUser = matchUserRepository
        .findByMatchIdAndUserIdWithFetch(matchId, request.getData().getUserId())
        .orElseThrow(() -> new ApiException(MatchStatus.NOT_PARTICIPATING_PLAYER));

    User user = matchUser.getUser();
    Match match = matchUser.getMatch();

    Team team = teamRepository.findByMatchIdAndUserId(matchId, user.getId()).orElseThrow(
        () -> new ApiException(TeamStatus.NOTFOUND_TEAM));

    MatchEvent matchEvent = MatchEventMapper.toEntity(request.getData(), match, user);
    matchEventRepository.save(matchEvent);

    MatchEventResponse matchEventResponse = MatchEventMapper.toResponse(matchEvent, team);
    messagingTemplate.convertAndSend("/topic/match/" + matchId, matchEventResponse);
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

  private void validateRequest(Long matchId, Message<MatchEventRequest> request) {
    List<String> errorMessages = new ArrayList<>();

    if (request.getData().getUserId() == null) {
      errorMessages.add("userId는 필수 입력 값입니다.");
    }

    if (errorMessages.size() > 0) {
      DefaultStatus defaultStatus = DefaultStatus.BAD_REQUEST;
      defaultStatus.setCustomDescription(String.join("\n", errorMessages));
      throw new ApiException(defaultStatus);
    }
  }
}
