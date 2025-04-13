package com.matchday.matchdayserver.team.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.team.model.dto.TeamCreateRequest;
import com.matchday.matchdayserver.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "팀 생성")
    @PostMapping
    public ApiResponse<?> createUser(@RequestBody TeamCreateRequest request) {
        teamService.create(request);
        return ApiResponse.ok("팀생성완료");
    }
}
