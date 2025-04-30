package com.matchday.matchdayserver.matchmemo;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.matchmemo.model.dto.MatchMemoRequest;
import com.matchday.matchdayserver.matchmemo.model.dto.MatchMemoResponse;
import com.matchday.matchdayserver.matchmemo.service.MatchMemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MatchMemo", description = "경기 팀 메모 API")
@RestController
@RequestMapping("/api/v1/matches/{matchId}/teams/{teamId}/memo")
@RequiredArgsConstructor
public class MatchMemoController {

  private final MatchMemoService memoService;

  @Operation(summary = "경기 팀 메모 등록/수정", description = "특정 경기의 특정 팀에 대한 메모를 생성하거나 수정합니다.")
  @PostMapping
  public ApiResponse<Void> createOrUpdate(
      @Parameter(description = "경기 ID") @PathVariable Long matchId,
      @Parameter(description = "팀 ID") @PathVariable Long teamId,
      @RequestBody MatchMemoRequest request) {
    memoService.createOrUpdate(matchId, teamId, request);
    return ApiResponse.ok(null);
  }

  @Operation(summary = "경기 팀 메모 조회", description = "특정 경기의 특정 팀에 대한 메모를 조회합니다.")
  @GetMapping
  public ApiResponse<MatchMemoResponse> get(
      @Parameter(description = "경기 ID") @PathVariable Long matchId,
      @Parameter(description = "팀 ID") @PathVariable Long teamId) {
    return ApiResponse.ok(memoService.get(matchId, teamId));
  }

  @Operation(summary = "경기 팀 메모 삭제", description = "특정 경기의 특정 팀에 대한 메모를 삭제합니다.")
  @DeleteMapping
  public ApiResponse<Void> delete(
      @Parameter(description = "경기 ID") @PathVariable Long matchId,
      @Parameter(description = "팀 ID") @PathVariable Long teamId) {
    memoService.delete(matchId, teamId);
    return ApiResponse.ok(null);
  }
}
