package com.matchday.matchdayserver.team.model.entity;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

public class MockTeam {
    /**
     * 모킹 된 팀 객체를 생성합니다.
     * @param teamId
     * @return 
     */
    public static Team create(Long teamId) {
        Team team = mock(Team.class);
        lenient().when(team.getId()).thenReturn(teamId);
        lenient().when(team.getName()).thenReturn("테스트 팀");
        lenient().when(team.getTeamImg()).thenReturn("team-image.jpg");
        lenient().when(team.getTeamColor()).thenReturn("#FFFFFF");
        lenient().when(team.getBottomColor()).thenReturn("#000000");
        lenient().when(team.getStockingColor()).thenReturn("#FF0000");
        return team;
    }

    /**
     * E2E 테스트관리트용 팀 객체를 생성합니다.
     * @param name
     * @return Team 객체
     */
    public static Team create(String name) {
        return Team.builder()
                .name(name)
                .teamImg("team-image.jpg")
                .teamColor("#FFFFFF")
                .bottomColor("#000000")
                .stockingColor("#FF0000")
                .build();
    }
}