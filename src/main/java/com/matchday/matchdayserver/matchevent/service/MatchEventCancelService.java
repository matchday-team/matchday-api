package com.matchday.matchdayserver.matchevent.service;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.model.enums.MatchState;
import com.matchday.matchdayserver.match.repository.MatchRepository;
import com.matchday.matchdayserver.match.util.MatchStateValidator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.matchday.matchdayserver.matchevent.model.dto.MatchEventCancelRequest;
import com.matchday.matchdayserver.matchevent.model.entity.MatchEvent;
import com.matchday.matchdayserver.matchevent.repository.MatchEventRepository;

import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchEventCancelService {

    private final MatchEventRepository matchEventRepository;
    private final MatchRepository matchRepository;

    public void cancelEvent(Long matchId, MatchEventCancelRequest message) {

        Match match = matchRepository.findById(matchId)
            .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH));

        MatchStateValidator.validateInPlay(match);

        MatchEvent matchEvent;
        MatchEventCancelRequest request = message;
        if (request.getMatchUserId() != null) {
            matchEvent = matchEventRepository.findFirstByMatchUserIdAndMatchIdAndEventTypeOrderByEventTimeDesc(
                    request.getMatchUserId(), matchId, request.getMatchEventType())
                .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH_EVENT));
        } else {
            matchEvent = matchEventRepository.findByMatchIdAndTeamIdAndEventType(
                    matchId, request.getTeamId(), request.getMatchEventType().name())
                .orElseThrow(() -> new ApiException(MatchStatus.NOTFOUND_MATCH_EVENT));
        }

        //파생 이벤트 삭제
        List<MatchEvent> childEvents = matchEventRepository.findAllByParentId(matchEvent.getId());
        matchEventRepository.deleteAll(childEvents);

        // 메인 이벤트 삭제
        matchEventRepository.delete(matchEvent);

    }
}
