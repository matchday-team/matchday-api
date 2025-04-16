package com.matchday.matchdayserver.userteam.model.mapper;

import com.matchday.matchdayserver.userteam.model.dto.JoinUserTeamResponse;
import com.matchday.matchdayserver.userteam.model.entity.UserTeam;

public class UserTeamMapper {
    public static JoinUserTeamResponse toJoinUserTeamResponse(UserTeam userTeam) {
        return JoinUserTeamResponse.builder()
                .id(userTeam.getId())
                .userName(userTeam.getUser().getName())
                .teamName(userTeam.getTeam().getName())
                .number(userTeam.getNumber())
                .defaultPosition(userTeam.getDefaultPosition())
                .build();
    }
}
