package com.matchday.matchdayserver.matchuser.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.matchday.matchdayserver.common.response.DefaultStatus;
import com.matchday.matchdayserver.matchevent.mapper.MatchEventMapper;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventResponse;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;
import com.matchday.matchdayserver.matchevent.repository.MatchEventRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserExchangeRequest;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.matchuser.repository.MatchUserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchUserExchangeService {

    private final MatchUserRepository matchUserRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MatchEventRepository matchEventRepository;

    public void exchangeMatchUser(Long matchId, MatchUserExchangeRequest request) {
        validateRequest(request);

        MatchUser fromMatchUser = matchUserRepository
            .findByMatchIdAndMatchUserIdWithFetch(matchId, request.getFromMatchUserId())
            .orElseThrow(() -> new ApiException(MatchStatus.NOT_PARTICIPATING_PLAYER));

        MatchUser toMatchUser = matchUserRepository
            .findByMatchIdAndMatchUserIdWithFetch(matchId, request.getToMatchUserId())
            .orElseThrow(() -> new ApiException(MatchStatus.NOT_PARTICIPATING_PLAYER));

        // Dirty Checking으로 자동 저장
        fromMatchUser.substituteTo(toMatchUser);

        List<MatchEvent> matchEvents = List.of(
            MatchEvent.builder()
                .eventTime(LocalDateTime.now())
                .eventType(MatchEventType.SUB_IN)
                .description(request.getMessage())
                .match(toMatchUser.getMatch())
                .matchUser(toMatchUser)
                .build(),
            MatchEvent.builder()
                .eventTime(LocalDateTime.now())
                .eventType(MatchEventType.SUB_OUT)
                .description(request.getMessage())
                .match(toMatchUser.getMatch())
                .matchUser(fromMatchUser)
                .build());
        matchEventRepository.saveAll(matchEvents);

        List<MatchEventResponse> matchEventResponses = matchEvents.stream()
            .map(MatchEventMapper::toResponse)
            .toList();

        for (MatchEventResponse response : matchEventResponses) {
            messagingTemplate.convertAndSend("/topic/match/" + matchId, response);
        }
    }

    private void validateRequest(MatchUserExchangeRequest request) {
        List<String> errorMessages = new ArrayList<>();

        if (request.getFromMatchUserId() == null
            || request.getToMatchUserId() == null) {
            errorMessages.add("fromMatchUserId, toUserId는 필수 입력 값입니다.");
        }

        if (!errorMessages.isEmpty()) {
            DefaultStatus defaultStatus = DefaultStatus.BAD_REQUEST;
            defaultStatus.setCustomDescription(String.join("\n", errorMessages));
            throw new ApiException(defaultStatus);
        }
    }
}
