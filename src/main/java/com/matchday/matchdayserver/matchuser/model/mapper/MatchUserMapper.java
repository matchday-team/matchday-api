package com.matchday.matchdayserver.matchuser.model.mapper;

import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserCreateRequest;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserEventStat;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserResponse;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.userteam.model.entity.UserTeam;

public class MatchUserMapper {
    //MatchUser Dto -> Entity 변환
    public static MatchUser toMatchUser(Match match, User user, MatchUserCreateRequest request) {
        return MatchUser.builder()
                .match(match)
                .user(user)
                .role(request.getRole())
                .matchPosition(request.getMatchPosition())
                .matchGrid(request.getMatchGrid())
                .build();
    }

    // MatchUser + UserTeam + EventStat → MatchUserResponse (DTO)변환
    public static MatchUserResponse toMatchUserResponse(MatchUser matchUser, UserTeam userTeam, MatchUserEventStat stat) {
      return MatchUserResponse.builder()
          .id(matchUser.getUser().getId())
          .name(matchUser.getUser().getName())
          .number(userTeam.getNumber())
          .matchPosition(matchUser.getMatchPosition())
          .matchGrid(matchUser.getMatchGrid())
          .goals(stat.getGoals())
          .assists(stat.getAssists())
          .fouls(stat.getFouls())
          .build();
    }
}
