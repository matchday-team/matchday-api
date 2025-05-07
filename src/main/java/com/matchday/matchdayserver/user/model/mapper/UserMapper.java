package com.matchday.matchdayserver.user.model.mapper;

import com.matchday.matchdayserver.user.model.dto.response.UserInfoResponse;
import com.matchday.matchdayserver.user.model.entity.User;

import java.util.List;

public class UserMapper {
  public static UserInfoResponse userInfoResponse(User user, List<Long> teamIds, List<Long> matchIds) {
    return UserInfoResponse.builder()
        .userId(user.getId())
        .userName(user.getName())
        .teamIds(teamIds)
        .matchIds(matchIds)
        .profileImg(user.getProfileImg())
        .build();
  }
}