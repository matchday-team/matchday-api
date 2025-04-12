package com.matchday.matchdayserver.user.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.user.model.Entity.User;
import com.matchday.matchdayserver.user.model.dto.request.UserCreateRequest;
import com.matchday.matchdayserver.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @Operation(summary = "임시 유저 생성")
    @PostMapping("/create")
    public ApiResponse<?> createUser(@RequestBody UserCreateRequest request) {
        //***임시*** 원래는 서비스단에서 처리할 내용
        User user = new User(request.getName());
        userRepository.save(user);

        return ApiResponse.ok("유저생성완료");
    }
}
