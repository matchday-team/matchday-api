package com.matchday.matchdayserver.team.model.mapper;

import com.matchday.matchdayserver.team.model.dto.response.TeamResponse;
import com.matchday.matchdayserver.team.model.dto.response.TeamSearchResponse;
import com.matchday.matchdayserver.team.model.entity.Team;

import java.util.List;
import java.util.stream.Collectors;

public class TeamMapper {
    //Team -> TeamResponse 변환 (팀 정보 조회)
    public static TeamResponse toTeamResponse(Team team) {
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .teamColor(team.getTeamColor())
                .build();
    }

    //Team -> TeamSearchResponse 변환 (팀 검색/조회)
    public static TeamSearchResponse toTeamSearchResponse(Team team) {
        return TeamSearchResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .build();
    }

    //Team 리스트 -> TeamSearchResponse 리스트로 변환 (팀 검색/조회 리스트)
    public static List<TeamSearchResponse> toTeamSearchResponseList(List<Team> teams) {
        return teams.stream()
                .map(TeamMapper::toTeamSearchResponse)
                .collect(Collectors.toList());
    }
}
