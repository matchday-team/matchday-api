package com.matchday.matchdayserver.matchevent.model.entity;

import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.team.model.entity.Team;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

public class MockMatchEvent {
    public static MatchEvent create(Team team) {
        MatchEvent event = mock(MatchEvent.class);
        MatchUser matchUser = mock(MatchUser.class);

        lenient().when(event.getEventType()).thenReturn(MatchEventType.GOAL);
        lenient().when(event.getMatchUser()).thenReturn(matchUser);
        lenient().when(matchUser.getTeam()).thenReturn(team);

        return event;
    }
}