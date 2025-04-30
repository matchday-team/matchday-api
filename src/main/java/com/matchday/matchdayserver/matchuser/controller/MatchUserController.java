package com.matchday.matchdayserver.matchuser.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserCreateRequest;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserResponse;
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
    public ApiResponse<String> createMatch(@PathVariable Long matchId,
                                           @RequestBody MatchUserCreateRequest request) {
      matchUserService.create(matchId, request);
        return ApiResponse.ok("매치 유저 등록 완료");
    }

    @Operation(summary = "매치에 출전한 선수들 조회", description = "경기 id 와 조회할 팀 id를 요청받습니다")
    @GetMapping("/{matchId}/players")
    public ApiResponse<List<MatchUserResponse>> getMatchUsers(@PathVariable Long matchId,@RequestParam Long teamId) {
      //todo 특정 role에 대해서만 조회 teamid 어떻게 할지
      List<MatchUserResponse> MatchUserResponses =  matchUserService.getMatchUsers(matchId,teamId);
      return ApiResponse.ok(MatchUserResponses);
    }
}
