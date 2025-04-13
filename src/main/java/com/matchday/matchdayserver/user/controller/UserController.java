package com.matchday.matchdayserver.user.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.user.model.dto.request.UserCreateRequest;
import com.matchday.matchdayserver.user.model.dto.request.UserJoinTeamRequest;
import com.matchday.matchdayserver.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "임시 유저 생성")
    @PostMapping
    public ApiResponse<?> createUser(@RequestBody UserCreateRequest request) {

        userService.createUser(request);
        return ApiResponse.ok("유저생성완료");
    }

    @Operation(summary = "팀 입단")
    @PostMapping("/{userId}/teams")
    public ApiResponse<?> joinTeam(@PathVariable Long userId,@RequestBody UserJoinTeamRequest request) {
        userService.joinTeam(userId,request);
        return ApiResponse.ok("팀 입단 완료");
    }
}
