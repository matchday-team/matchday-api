package com.matchday.matchdayserver.matchuser.model.dto;

import com.matchday.matchdayserver.matchuser.model.dto.MatchUserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "홈팀과 어웨이팀 참가자 정보를 선발/교체로 그룹핑한 응답")
public class MatchUserGroupResponse {

    @Schema(description = "홈팀 참가자 정보 (선발/교체)")
    private TeamGroupedUsers homeTeam;

    @Schema(description = "어웨이팀 참가자 정보 (선발/교체)")
    private TeamGroupedUsers awayTeam;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "선발 및 교체 참가자 그룹")
    public static class TeamGroupedUsers {

        @Schema(description = "선발 선수 목록")
        private List<MatchUserResponse> starters;

        @Schema(description = "교체 선수 목록")
        private List<MatchUserResponse> substitutes;
    }
}
