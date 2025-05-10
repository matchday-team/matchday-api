package com.matchday.matchdayserver.matchevent.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.model.Message;
import com.matchday.matchdayserver.common.response.DefaultStatus;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.common.response.UserStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventRequest;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventResponse;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventUserRequest;
import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
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

    public void saveMatchEvent(Long matchId, Message<MatchEventUserRequest> request) {
        validateRequest(matchId, request);
        validateAuthUser(matchId, request.getToken());

        MatchUser matchUser = matchUserRepository
            .findByMatchIdAndUserIdWithFetch(matchId, request.getData().getUserId())
            .orElseThrow(() -> new ApiException(MatchStatus.NOT_PARTICIPATING_PLAYER));

        Match match = matchUser.getMatch();

        List<MatchEventResponse> matchEventResponse = matchEventStrategy
            .generateMatchEventLog(request.getData(), match, matchUser);
        for (MatchEventResponse response : matchEventResponse) {
            if (!neededToSendEvent(response.getEventLog())) continue;
            messagingTemplate.convertAndSend("/topic/match/" + matchId, response);
        }
    }

    private boolean neededToSendEvent(MatchEventType matchEventType) {
        return MatchEventType.GOAL.equals(matchEventType)
            || MatchEventType.ASSIST.equals(matchEventType)
            || MatchEventType.OFFSIDE.equals(matchEventType)
            || MatchEventType.YELLOW_CARD.equals(matchEventType)
            || MatchEventType.RED_CARD.equals(matchEventType)
            || MatchEventType.SUB_IN.equals(matchEventType)
            || MatchEventType.SUB_OUT.equals(matchEventType)
            ;
    }

    private void validateAuthUser(Long matchId, String token) {
        Long authId = Long.parseLong(token);

        MatchUser authUser = matchUserRepository
            .findById(authId)
            .orElseThrow(() -> new ApiException(UserStatus.NOTFOUND_USER));

        if (!authUser.getMatch().getId().equals(matchId)) {
            throw new ApiException(MatchStatus.NOT_PARTICIPATING_PLAYER);
        }
    }

    private void validateRequest(Long matchId, Message<MatchEventUserRequest> request) {
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
