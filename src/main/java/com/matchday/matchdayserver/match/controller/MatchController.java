package com.matchday.matchdayserver.match.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.match.model.dto.request.MatchCreateRequest;
import com.matchday.matchdayserver.match.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @Operation(summary = "매치 생성")
    @PostMapping
    public ApiResponse<String> createMatch(@RequestBody MatchCreateRequest request) {
        matchService.create(request);
        return ApiResponse.ok("매치 생성 완료");
    }
}
