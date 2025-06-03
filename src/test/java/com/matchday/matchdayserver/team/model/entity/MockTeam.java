package com.matchday.matchdayserver.team.model.entity;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

public class MockTeam {
    public static Team create(Long teamId) {
        Team team = mock(Team.class);
        lenient().when(team.getId()).thenReturn(teamId);
        lenient().when(team.getName()).thenReturn("테스트 팀");
        lenient().when(team.getTeamImg()).thenReturn("team-image.jpg");
        return team;
    }

}