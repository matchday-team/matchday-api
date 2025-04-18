package com.matchday.matchdayserver.userteam.model.mapper;

import com.matchday.matchdayserver.team.model.dto.response.TeamMemberListResponse;
import com.matchday.matchdayserver.team.model.dto.response.TeamMemberResponse;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.userteam.model.dto.JoinUserTeamResponse;
import com.matchday.matchdayserver.userteam.model.entity.UserTeam;

public class UserTeamMapper {
    //userTeam -> JoinUserTeam Response 변환 정적 메소드
    public static JoinUserTeamResponse toJoinUserTeamResponse(UserTeam userTeam) {
        return JoinUserTeamResponse.builder()
                .id(userTeam.getId())
                .userName(userTeam.getUser().getName())
                .teamName(userTeam.getTeam().getName())
                .number(userTeam.getNumber())
                .defaultPosition(userTeam.getDefaultPosition())
                .build();
    }
    //userTeam -> TeamMember Response 변환 정적 메소드
    public static TeamMemberResponse toTeamMemberResponse(UserTeam userTeam) {
        User user = userTeam.getUser();
        return TeamMemberResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .number(userTeam.getNumber())
                .defaultPosition(userTeam.getDefaultPosition())
                .isActive(userTeam.getIsActive())
                .build();
    }
}
