package com.matchday.matchdayserver.match.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.match.model.dto.request.MatchCreateRequest;
import com.matchday.matchdayserver.match.model.dto.request.MatchMemoRequest;
import com.matchday.matchdayserver.match.model.dto.response.MatchInfoResponse;
import com.matchday.matchdayserver.match.model.dto.response.MatchListResponse;
import com.matchday.matchdayserver.match.model.dto.response.MatchScoreResponse;
import com.matchday.matchdayserver.match.model.dto.response.MatchMemoResponse;
import com.matchday.matchdayserver.match.service.MatchCreateService;
import com.matchday.matchdayserver.match.service.MatchScoreService;
import com.matchday.matchdayserver.match.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "matches", description = "매치 관련 API")
@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchCreateService matchCreateService;
    private final MatchService matchService;
    private final MatchScoreService matchScoreService;

    @Operation(summary = "매치 생성")
    @PostMapping
    public ApiResponse<Long> createMatch(@RequestBody MatchCreateRequest request) {
        Long id = matchCreateService.create(request);
        return ApiResponse.ok(id);
    }

    @GetMapping("/{matchId}")
    @Operation(summary = "매치 정보 조회")
    public ApiResponse<MatchInfoResponse> getMatchInfo(@PathVariable Long matchId) {
        MatchInfoResponse response = matchService.getMatchInfo(matchId);
        return ApiResponse.ok(response);
    }

    @GetMapping("/{matchId}/score")
    @Operation(summary = "매치 점수 조회")
    public ApiResponse<MatchScoreResponse> getMatchScore(@PathVariable Long matchId) {
        MatchScoreResponse response = matchScoreService.getMatchScore(matchId);
        return ApiResponse.ok(response);
    }

    @Operation(summary = "매치 팀 메모 등록/수정", description = "특정 경기의 특정 팀에 대한 메모를 생성하거나 수정합니다 null 값 입력시 메모 초기화.")
    @PostMapping("{matchId}/teams/{teamId}/memo")
    public ApiResponse<String> createOrUpdate(
        @Parameter(description = "경기 ID") @PathVariable Long matchId,
        @Parameter(description = "팀 ID") @PathVariable Long teamId,
        @RequestBody MatchMemoRequest request) {
      matchService.createOrUpdate(matchId, teamId, request);
      return ApiResponse.ok("메모 변경 완료");
    }

    @Operation(summary = "매치 팀 메모 조회", description = "특정 경기의 특정 팀에 대한 메모를 조회합니다.")
    @GetMapping("{matchId}/teams/{teamId}/memo")
    public ApiResponse<MatchMemoResponse> get(
        @Parameter(description = "경기 ID") @PathVariable Long matchId,
        @Parameter(description = "팀 ID") @PathVariable Long teamId) {
      return ApiResponse.ok(matchService.get(matchId, teamId));
    }

    @Operation(summary = "매치 리스트 조회", description = "특정 팀이 속한 매치 리스트를 조회합니다. <br> 경기 상태(matchStatus) 값은 SCHEDULED(경기 전), IN_PLAY(경기중), FINISHED(경기 종료) 입니다.")
    @GetMapping("/teams/{teamId}")
    public ApiResponse<List<MatchListResponse>> getMatchList(@PathVariable Long teamId) {
        return ApiResponse.ok(matchService.getMatchListByTeam(teamId));
    }
}
