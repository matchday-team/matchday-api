package com.matchday.matchdayserver.team.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.team.model.dto.request.TeamCreateRequest;
import com.matchday.matchdayserver.team.model.dto.response.TeamListResponse;
import com.matchday.matchdayserver.team.model.dto.response.TeamMemberListResponse;
import com.matchday.matchdayserver.team.model.dto.response.TeamMemberResponse;
import com.matchday.matchdayserver.team.model.dto.response.TeamNameResponse;
import com.matchday.matchdayserver.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "teams", description = "팀 관련 API")
@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "팀 생성", description = "팀 생성 API입니다. ")
    @PostMapping
    public ApiResponse<Long> createUser(@RequestBody TeamCreateRequest request) {
        Long teamId=teamService.create(request);
        return ApiResponse.ok(teamId);
    }

    @Operation(summary = "팀 검색")
    @GetMapping("/search")
    public ApiResponse<TeamListResponse> searchTeams(@RequestParam String keyword) {
        List<TeamNameResponse> teamList = teamService.searchTeams(keyword);
        TeamListResponse response = new TeamListResponse(teamList);
        return ApiResponse.ok(response);
    }

    @Operation(summary = "팀 목록 조회")
    @GetMapping()
    public ApiResponse<TeamListResponse> getTeam() {
        List<TeamNameResponse> teamList = teamService.getAllTeams();
        TeamListResponse response = new TeamListResponse(teamList);
        return ApiResponse.ok(response);
    }

    @Operation(summary = "팀에 속한 맴버 리스트 조회")
    @GetMapping("/{teamId}/users")
    public ApiResponse<TeamMemberListResponse> getTeamMembers(@PathVariable Long teamId) {
        return ApiResponse.ok(teamService.getTeamMembers(teamId));
    }
}
