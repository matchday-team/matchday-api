package com.matchday.matchdayserver.user.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Schema(description = "User 정보 응답 객체")
public class UserInfoResponse {
    @NotNull
    @Schema(description = "유저 ID")
    private Long userId;

    @NotNull
    @Schema(description = "유저 이름")
    private String userName;

    @Schema(description = "프로필 이미지" ,nullable = true)
    private String profileImg;

    @NotNull
    @Schema(description = "유저가 소속된 팀 ID")
    private List<Long> teamIds;

    @NotNull
    @Schema(description = "유저가 소속된 매치 ID")
    private List<Long> matchIds;

  @Builder
  public UserInfoResponse(Long userId, String userName, String profileImg ,List<Long> teamIds, List<Long> matchIds) {
    this.userId = userId;
    this.userName = userName;
    this.teamIds = teamIds;
    this.matchIds = matchIds;
    this.profileImg = profileImg;
  }
}
