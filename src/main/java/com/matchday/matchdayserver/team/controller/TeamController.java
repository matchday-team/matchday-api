package com.matchday.matchdayserver.team.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.team.model.dto.request.TeamCreateRequest;
import com.matchday.matchdayserver.team.model.dto.response.TeamBasicDto;
import com.matchday.matchdayserver.team.model.dto.response.TeamListResponse;
import com.matchday.matchdayserver.team.model.entity.Team;
import com.matchday.matchdayserver.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Operation(summary = "팀 검색")
    @GetMapping("/search")
    public ApiResponse<?> searchTeams(@RequestParam String keyword) {
        List<TeamBasicDto> teamList = teamService.searchTeams(keyword);
        TeamListResponse response = new TeamListResponse(teamList);
        return ApiResponse.ok(response);
    }

    @Operation(summary = "팀 목록 조회")
    @GetMapping()
    public ApiResponse<?> getTeam() {
        List<TeamBasicDto> teamList = teamService.getAllTeams();
        TeamListResponse response = new TeamListResponse(teamList);
        return ApiResponse.ok(response);
    }
}
