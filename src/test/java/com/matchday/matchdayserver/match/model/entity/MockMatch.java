package com.matchday.matchdayserver.match.model.entity;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import com.matchday.matchdayserver.match.model.enums.MatchState;
import com.matchday.matchdayserver.team.model.entity.Team;
import java.time.LocalDate;

public class MockMatch {

    public static Match create(Long matchId, Team homeTeam, Team awayTeam,
        MatchState state) {
        Match match = mock(Match.class);
        lenient().when(match.getId()).thenReturn(matchId);
        lenient().when(match.getHomeTeam()).thenReturn(homeTeam);
        lenient().when(match.getAwayTeam()).thenReturn(awayTeam);
        lenient().when(match.getMatchState()).thenReturn(state);
        lenient().when(match.getMatchDate()).thenReturn(LocalDate.now().minusDays(matchId));
        return match;
    }
}