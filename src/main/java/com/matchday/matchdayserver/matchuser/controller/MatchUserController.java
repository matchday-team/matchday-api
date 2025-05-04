package com.matchday.matchdayserver.matchuser.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserCreateRequest;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserGroupResponse;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserResponse;
import com.matchday.matchdayserver.matchuser.model.mapper.MatchUserMapper;
import com.matchday.matchdayserver.matchuser.service.MatchUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "match-users", description = "매치 유저 관련 API")
@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchUserController {
    private final MatchUserService matchUserService;

    @Operation(summary = "매치 유저 등록", description = "{matchID}에 사용자(user)가 등록됩니다.")
    @PostMapping("/{matchId}/users")
    public ApiResponse<Long> createMatch(@PathVariable Long matchId,
                                           @RequestBody MatchUserCreateRequest request) {
        Long id = matchUserService.create(matchId, request);
        return ApiResponse.ok(id);
    }

    @Operation(summary = "매치에 출전한 선수들 조회", description = "경기 id를 입력받아 hometeam/awayteam 으로 그룹핑하여 응답합니다")
    @GetMapping("/{matchId}/players")
    public ApiResponse<MatchUserGroupResponse> getGroupedMatchUsers(@PathVariable Long matchId) {
        MatchUserGroupResponse groupedUsers = matchUserService.getGroupedMatchUsers(matchId);
        return ApiResponse.ok(groupedUsers);
    }
}
