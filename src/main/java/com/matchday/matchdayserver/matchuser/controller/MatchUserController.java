package com.matchday.matchdayserver.matchuser.controller;

import com.matchday.matchdayserver.common.annotation.UserId;
import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserCreateRequest;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserGridUpdateRequest;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserGroupResponse;
import com.matchday.matchdayserver.matchuser.service.MatchUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "match-users", description = "매치 유저 관련 API")
@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchUserController {
    private final MatchUserService matchUserService;

    @Operation(summary = "매치 유저 등록", description = "{matchID}에 사용자(user)가 등록됩니다.")
    @PostMapping("/{matchId}/users")
    public ApiResponse<Long> createMatch(@UserId Long userId, @PathVariable Long matchId,
                                           @RequestBody @Valid MatchUserCreateRequest request) {
        Long id = matchUserService.create(matchId, request);
        return ApiResponse.ok(id);
    }

    @Operation(
        summary = "매치 참가 선수 목록 조회",
        description = """
            경기 ID를 기반으로 출전 선수 정보를 홈팀/어웨이팀으로 그룹핑하여 반환합니다. 
            각 팀은 선발(starters)과 교체(substitutes) 선수 목록으로 구분되며, 
            선수별로 득점, 어시스트, 옐로카드/레드카드 수, 경고 누적 수(caution), 퇴장 여부(sentOff) 정보가 포함됩니다.
            FIFA 룰 기반 카드 처리 기준을 따릅니다. 경고 누적 수는 옐로 카드로만 결정되며 레드카드 받았을 시 경고가 누적되지 않고 바로 퇴장입니다
        """
    )
    @GetMapping("/{matchId}/players")
    public ApiResponse<MatchUserGroupResponse> getGroupedMatchUsers(@UserId Long userId,
        @Parameter(description = "매치 ID", example = "1", required = true)
        @PathVariable Long matchId
    ) {
        MatchUserGroupResponse groupedUsers = matchUserService.getGroupedMatchUsers(matchId);
        return ApiResponse.ok(groupedUsers);
    }

    @Operation(summary = "매치유저 그리드 좌표 변경", description = "그리드 좌표 값은 0~29 사이의 값이어야합니다.")
    @PatchMapping("/{matchUserId}/grid")
    public ApiResponse<String> updateMatchUserGrid(
        @PathVariable Long matchUserId,
        @RequestBody @Valid MatchUserGridUpdateRequest request
    ) {
        matchUserService.updateMatchUserGrid(matchUserId, request);
        return ApiResponse.ok("그리드 수정 완료");
    }

    @Operation(summary = "매치 유저 삭제")
    @DeleteMapping("/{matchUserId}")
    public ApiResponse<String> deleteMatchUser(@PathVariable Long matchUserId) {
        matchUserService.matchUserDelete(matchUserId);
        return ApiResponse.ok("매치 유저 삭제 완료");
    }
}
