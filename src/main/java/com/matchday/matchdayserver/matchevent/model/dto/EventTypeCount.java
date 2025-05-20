package com.matchday.matchdayserver.matchevent.model.dto;
import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;

public interface EventTypeCount {
    MatchEventType getEventType();  // enum으로 변경
    Long getCount();
}
