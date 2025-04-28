package com.matchday.matchdayserver.match.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.match.model.dto.request.MatchCreateRequest;
import com.matchday.matchdayserver.match.model.dto.response.MatchInfoResponse;
import com.matchday.matchdayserver.match.service.MatchCreateService;
import com.matchday.matchdayserver.match.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "matches", description = "매치 관련 API")
@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchCreateService matchCreateService;
    private final MatchService matchService;

    @Operation(summary = "매치 생성")
    @PostMapping
    public ApiResponse<String> createMatch(@RequestBody MatchCreateRequest request) {
        matchCreateService.create(request);
        return ApiResponse.ok("매치 생성 완료");
    }

    @GetMapping("/{matchId}")
    @Operation(summary = "매치 정보 조회")
    public ApiResponse<MatchInfoResponse> getMatchInfo(@PathVariable Long matchId) {
      MatchInfoResponse response = matchService.getMatchInfo(matchId);
      return ApiResponse.ok(response);
    }
}
