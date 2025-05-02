package com.matchday.matchdayserver.matchevent.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventResponse;
import com.matchday.matchdayserver.matchevent.service.MatchEventHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "matches", description = "매치 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/matches")
public class MatchEventHistoryController {

    private final MatchEventHistoryService matchService;

    @GetMapping("/{matchId}/history")
    @Operation(summary = "매치 이벤트 이력 조회")
    public ApiResponse<List<MatchEventResponse>> getMatchEventHistory(@PathVariable Long matchId) {
        return ApiResponse.ok(matchService.findAllHistoryByMatchId(matchId));
    }
}
