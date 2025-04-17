package com.matchday.matchdayserver.matchplayer.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.matchplayer.model.dto.MatchPlayerCreateRequest;
import com.matchday.matchdayserver.matchplayer.service.MatchPlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "match-players", description = "매치 선수 관련 API")
@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchPlayerController {
    private final MatchPlayerService matchPlayerService;

    @Operation(summary = "매치 선수 등록")
    @PostMapping("/{matchId}/players")
    public ApiResponse<String> createMatch(@PathVariable Long matchId,
                                           @RequestBody MatchPlayerCreateRequest request) {
        matchPlayerService.create(matchId, request);
        return ApiResponse.ok("매치 선수 등록 완료");
    }
}
