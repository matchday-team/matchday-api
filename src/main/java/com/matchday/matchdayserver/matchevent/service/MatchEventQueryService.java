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
    public MatchUserEventStat getMatchUserEventStat(Long matchId,Long matchUserId) {
        int goals=0;
        int assists=0;
        int cards=0;

        //쿼리 결과 받아서 변수에 대입
        List<EventTypeCount> eventTypeCounts= matchEventRepository.countEventTypesByMatchUserAndMatch(matchId,matchUserId);
        for (EventTypeCount eventTypeCount : eventTypeCounts) {
            String eventType = eventTypeCount.getEventType();
            Long count = eventTypeCount.getCount();

            if ("GOALS".equals(eventType)) {
                goals = count.intValue();
            } else if ("ASSISTS".equals(eventType)) {
                assists = count.intValue();
            } else if ("YELLOW_CARD".equals(eventType)||"RED_CARD".equals(eventType)) {
                cards += count.intValue();
            }
        }

        //쿼리로 얻은 변수로 DTO 만들어 결과값으로 리턴
        return MatchUserEventStat.builder().
            goals(goals).
            assists(assists).
            cards(cards).
            build();
    }
}