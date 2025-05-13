package com.matchday.matchdayserver.matchevent.service;

import com.matchday.matchdayserver.matchevent.model.dto.EventTypeCount;
import com.matchday.matchdayserver.matchevent.repository.MatchEventRepository;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserEventStat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchEventQueryService {

    private final MatchEventRepository matchEventRepository;
    /**
     * MatchEventStrategy
     * <p>
     * 특정 경기에서 특정 선수에 대한 득점,어시스트,파울 개수를 조회합니다
     * <p>
     */
    public MatchUserEventStat getMatchUserEventStat(Long matchId, Long matchUserId) {
        int goals = 0, assists = 0, yellowCards = 0, redCards = 0;

        List<EventTypeCount> counts = matchEventRepository.countEventTypesByMatchUserAndMatch(matchUserId, matchId);
        for (EventTypeCount c : counts) {
            switch (c.getEventType()) {
                case "GOAL" -> goals = c.getCount().intValue();
                case "ASSIST" -> assists = c.getCount().intValue();
                case "YELLOW_CARD" -> yellowCards = c.getCount().intValue();
                case "RED_CARD" -> redCards = c.getCount().intValue();
            }
        }

        return MatchUserEventStat.builder()
            .goals(goals)
            .assists(assists)
            .yellowCards(yellowCards)
            .redCards(redCards)
            .caution(yellowCards)                 // caution == yellowCards
            .sentOff(yellowCards >= 2 || redCards >= 1)  // 퇴장 판단
            .build();
    }
}