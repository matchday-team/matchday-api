package com.matchday.matchdayserver.user.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class UserInfoResponse {
  private Long userId;
  private String userName;
  private List<Long> teamIds;
  private List<Long> matchIds;

  @Builder
  public UserInfoResponse(Long userId, String userName ,List<Long> teamIds, List<Long> matchIds) {
    this.userId = userId;
    this.userName = userName;
    this.teamIds = teamIds;
    this.matchIds = matchIds;
  }
}
