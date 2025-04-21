package com.matchday.matchdayserver.team.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.team.model.dto.request.TeamCreateRequest;
import com.matchday.matchdayserver.team.model.dto.response.TeamResponse;
import com.matchday.matchdayserver.team.model.dto.response.TeamSearchListResponse;
import com.matchday.matchdayserver.team.model.dto.response.TeamMemberListResponse;
import com.matchday.matchdayserver.team.model.dto.response.TeamSearchResponse;
import com.matchday.matchdayserver.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<Long> createTeam(@RequestBody @Valid TeamCreateRequest request) {
        Long teamId = teamService.create(request);
        return ApiResponse.ok(teamId);
    }

    @Operation(summary = "팀 검색", description = "키워드로 팀 검색하는 API입니다.")
    @GetMapping("/search")
    public ApiResponse<TeamSearchListResponse> searchTeams(@RequestParam String keyword) {
        List<TeamSearchResponse> teamList = teamService.searchTeams(keyword);
        TeamSearchListResponse response = new TeamSearchListResponse(teamList);
        return ApiResponse.ok(response);
    }

    @Operation(summary = "팀 목록 조회", description = "전체 팀 조회 API입니다")
    @GetMapping()
    public ApiResponse<TeamSearchListResponse> getTeamList() {
        List<TeamSearchResponse> teamList = teamService.getAllTeams();
        TeamSearchListResponse response = new TeamSearchListResponse(teamList);
        return ApiResponse.ok(response);
    }

    @Operation(summary = "팀 정보 조회", description = "{timeId]의 정보 조회 API입니다.")
    @GetMapping("/{teamId}")
    public ApiResponse<TeamResponse> getTeamInfo(@PathVariable Long teamId) {
        TeamResponse response = teamService.getTeamInfo(teamId);
        return ApiResponse.ok(response);
    }

    @Operation(summary = "팀에 속한 맴버 리스트 조회")
    @GetMapping("/{teamId}/users")
    public ApiResponse<TeamMemberListResponse> getTeamMembers(@PathVariable Long teamId) {
        return ApiResponse.ok(teamService.getTeamMembers(teamId));
    }
}
