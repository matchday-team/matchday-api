package com.matchday.matchdayserver.team.model.mapper;

import com.matchday.matchdayserver.team.model.dto.response.TeamResponse;
import com.matchday.matchdayserver.team.model.entity.Team;

public class TeamMapper {
    public static TeamResponse toTeamResponse(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .teamColor(team.getTeamColor())
                .build();
    }
}
