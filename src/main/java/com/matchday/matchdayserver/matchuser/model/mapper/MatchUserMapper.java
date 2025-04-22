package com.matchday.matchdayserver.matchuser.model.mapper;

import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserCreateRequest;
import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import com.matchday.matchdayserver.user.model.entity.User;

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
}
