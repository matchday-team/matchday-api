package com.matchday.matchdayserver.matchevent.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.DefaultStatus;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.matchevent.common.MatchEventConstants;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventRequest;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventResponse;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventUserRequest;

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

    public void saveMatchEvent(Long matchId, MatchEventUserRequest request) {
        validateRequest(matchId, request);

        MatchUser matchUser = matchUserRepository.findByMatchIdAndMatchUserIdWithFetch(matchId,
                request.getMatchUserId())
            .orElseThrow(() -> new ApiException(MatchStatus.NOT_PARTICIPATING_PLAYER));

        Match match = matchUser.getMatch();

        List<MatchEventResponse> matchEventResponse = matchEventStrategy.generateMatchEventLog(
            request, match, matchUser);
        if (!matchEventResponse.isEmpty()) {
            messagingTemplate.convertAndSend(MatchEventConstants.getMatchEventUrl(matchId),
                matchEventResponse.get(0));
        }
    }

    public void saveMatchTeamEvent(Long matchId, Long teamId, MatchEventRequest request) {

        // 임시 유저
        MatchUser matchUser = matchUserRepository.findTempUser(matchId, teamId)
            .orElseThrow(() -> new ApiException(MatchStatus.NOT_PARTICIPATING_PLAYER));

        Match match = matchUser.getMatch();

        List<MatchEventResponse> matchEventResponse = matchEventStrategy.generateMatchEventLog(
            request, match, matchUser);
        if (!matchEventResponse.isEmpty()) {
            messagingTemplate.convertAndSend(MatchEventConstants.getMatchEventUrl(matchId),
                matchEventResponse.get(0));
        }
    }

    private void validateRequest(Long matchId, MatchEventUserRequest request) {
        List<String> errorMessages = new ArrayList<>();

        if (request.getMatchUserId() == null) {
            errorMessages.add("userId는 필수 입력 값입니다.");
        }

        if (matchId == null) {
            errorMessages.add("matchId는 필수 입력 값입니다.");
        }

        if (errorMessages.size() > 0) {
            DefaultStatus defaultStatus = DefaultStatus.BAD_REQUEST;
            defaultStatus.setCustomDescription(String.join("\n", errorMessages));
            throw new ApiException(defaultStatus);
        }
    }
}
