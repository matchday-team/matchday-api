package com.matchday.matchdayserver.user.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.user.model.dto.request.UserCreateRequest;
import com.matchday.matchdayserver.user.model.dto.request.UserJoinTeamRequest;
import com.matchday.matchdayserver.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "users", description = "유저 관련 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "임시 유저 생성" , description = "생성된 user의 Id 값 반환")
    @PostMapping
    public ApiResponse<Long> createUser(@RequestBody UserCreateRequest request) {
        Long userId=userService.createUser(request);
        return ApiResponse.ok(userId);
    }

    @Operation(summary = "팀 입단", description = "{userId}가 teamId에 소속됩니다 ")
    @PostMapping("/{userId}/teams")
    public ApiResponse<?> joinTeam(@PathVariable Long userId,@RequestBody UserJoinTeamRequest request) {
        return ApiResponse.ok(userService.joinTeam(userId,request));
    }
}
