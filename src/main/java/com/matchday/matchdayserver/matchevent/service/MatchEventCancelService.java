package com.matchday.matchdayserver.matchevent.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.matchday.matchdayserver.matchevent.model.dto.MatchEventCancelRequest;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.matchevent.repository.MatchEventRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchEventCancelService {

    private final MatchEventRepository matchEventRepository;

    public void cancelEvent(Long matchId, MatchEventCancelRequest message) {
        MatchEvent matchEvent;
        MatchEventCancelRequest request = message;
        if (request.getMatchUserId() != null) {
            matchEvent = matchEventRepository.findByMatchUserIdAndMatchIdAndEventType(
                    request.getMatchUserId(), matchId, request.getMatchEventType())
                .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH_EVENT));
        } else {
            matchEvent = matchEventRepository.findByMatchIdAndTeamIdAndEventType(
                    matchId, request.getTeamId(), request.getMatchEventType().name())
                .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH_EVENT));
        }
        matchEventRepository.delete(matchEvent);
    }
}
