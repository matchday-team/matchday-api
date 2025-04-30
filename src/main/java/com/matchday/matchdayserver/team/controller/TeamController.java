package com.matchday.matchdayserver.team.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.s3.service.S3PresignedService;
import com.matchday.matchdayserver.s3.dto.S3PresignedResponse;
import com.matchday.matchdayserver.team.model.dto.request.TeamCreateRequest;
import com.matchday.matchdayserver.team.model.dto.response.TeamResponse;
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
    private final S3PresignedService s3PresignedManager;
    private static final String FOLDER_NAME = "teams";

    @Operation(summary = "팀 생성", description = "팀 생성 API입니다. <br> 컬러는 모두 Hex Code로 입력 해주세요.")
    @PostMapping
    public ApiResponse<Long> createTeam(@RequestBody @Valid TeamCreateRequest request) {
        Long teamId = teamService.create(request);
        return ApiResponse.ok(teamId);
    }

    @Operation(summary = "팀 프로필 업로드 presigned URL 발급", description = "teamId와 확장자(jpg, jpeg, png, webp)를 넘겨주면 이미지 업로드용 URL과 저장할 FileName(파일명)을 반환합니다. <br> 반환 받은 파일명을 저장하세요(이미지 조회시 필요)")
    @GetMapping("/{teamId}/profile/upload-url")
    public ApiResponse<S3PresignedResponse> generateUploadUrl(
        @PathVariable Long teamId,
        @RequestParam String extension
    ) {
      return ApiResponse.ok(s3PresignedManager.generateUploadUrl(FOLDER_NAME, teamId, extension));
    }

    @Operation(summary = "팀 프로필 이미지 read presigned URL 조회", description = "team 프로필 업로드에서 반환 받은 파일명(key)을 넘겨주면 이미지 read용 URL을 반환합니다")
    @GetMapping("/{teamId}/profile/read-url")
    public ApiResponse<S3PresignedResponse> getProfileReadUrl(
        @PathVariable Long teamId,
        @RequestParam String key
    ) {
      return ApiResponse.ok(s3PresignedManager.generateReadUrl(FOLDER_NAME, teamId, key));
    }


    @Operation(summary = "팀 검색", description = "키워드로 팀 검색하는 API입니다.")
    @GetMapping("/search")
    public ApiResponse<List<TeamSearchResponse>> searchTeams(@RequestParam String keyword) {
        List<TeamSearchResponse> teamList = teamService.searchTeams(keyword);
        return ApiResponse.ok(teamList);
    }

    @Operation(summary = "팀 목록 조회", description = "전체 팀 조회 API입니다")
    @GetMapping
    public ApiResponse<List<TeamSearchResponse>> getTeamList() {
        List<TeamSearchResponse> teamList = teamService.getAllTeams();
        return ApiResponse.ok(teamList);
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
