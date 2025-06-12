package com.matchday.matchdayserver.user.controller;

import com.matchday.matchdayserver.common.annotation.UserId;
import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.s3.service.S3PresignedService;
import com.matchday.matchdayserver.s3.dto.S3PresignedResponse;
import com.matchday.matchdayserver.user.model.dto.request.UpdateUserRoleRequest;
import com.matchday.matchdayserver.user.model.dto.request.UserCreateRequest;
import com.matchday.matchdayserver.user.model.dto.request.UserJoinTeamRequest;
import com.matchday.matchdayserver.user.model.dto.response.UpdateUserRoleResponse;
import com.matchday.matchdayserver.user.model.dto.response.UserInfoResponse;
import com.matchday.matchdayserver.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "users", description = "유저 관련 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final S3PresignedService s3PresignedManager;
    private static final String FOLDER_NAME = "users";

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "임시 유저 생성",
        description = """
            임시 유저를 생성하고, 생성된 유저의 ID를 반환합니다.  
            프로필 이미지는 선택 사항입니다.  
            [필요 권한] ADMIN
            """
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Long> createUser(@Valid @RequestBody UserCreateRequest request) {
        Long userId = userService.createUser(request);
        return ApiResponse.created(userId);
    }

    @Operation(
        summary = "팀 입단",
        description = """
            지정한 유저(userId)가 특정 팀(teamId)에 소속됩니다.  
            [필요 권한] 없음 (추후 별도의 팀 코드를 통해 인증 예정)
            """
    )
    @PostMapping("/{userId}/teams")
    public ApiResponse<?> joinTeam(@PathVariable("userId") Long userId, @RequestBody UserJoinTeamRequest request) {
        return ApiResponse.ok(userService.joinTeam(userId, request));
    }

    @Operation(
        summary = "유저 프로필 업로드 presigned URL 발급",
        description = """
            userId와 확장자(jpg, jpeg, png, webp)를 전달하면  
            이미지 업로드용 presigned URL과 저장할 파일명(key)을 반환합니다.  
            반환받은 파일명은 반드시 저장해주세요. (이미지 조회 시 필요)  
            [필요 권한] 없음
            """
    )
    @GetMapping("/{userId}/profile/upload-url")
    public ApiResponse<S3PresignedResponse> generateUploadUrl(
        @PathVariable Long userId,
        @RequestParam String extension
    ) {
        return ApiResponse.ok(s3PresignedManager.generateUploadUrl(FOLDER_NAME, userId, extension));
    }

    @Operation(
        summary = "유저 프로필 이미지 read presigned URL 조회",
        description = """
            프로필 업로드 시 반환받은 파일명(key)을 전달하면  
            이미지 조회용 presigned URL을 반환합니다.  
            [필요 권한] 없음
            """
    )
    @GetMapping("/{userId}/profile/read-url")
    public ApiResponse<S3PresignedResponse> getProfileReadUrl(
        @PathVariable Long userId,
        @RequestParam String key
    ) {
        return ApiResponse.ok(s3PresignedManager.generateReadUrl(FOLDER_NAME, userId, key));
    }

    @Operation(
        summary = "유저 정보 조회",
        description = """
            특정 유저의 이름, 소속 팀 ID, 참여한 매치 ID들을 조회합니다.  
            [필요 권한] ADMIN
            """
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ApiResponse<UserInfoResponse> getUserInfo(@PathVariable("userId") Long userId) {
        return ApiResponse.ok(userService.getUserInfo(userId));
    }

    @Operation(
        summary = "로그아웃",
        description = """
            현재 로그인된 사용자의 리프레시 토큰을 무효화합니다.  
            [필요 권한] 없음
            """
    )
    @PostMapping("/logout")
    public ApiResponse<Long> logout(@UserId Long userId) {
        userService.logout(userId);
        return ApiResponse.ok(userId);
    }

    @Operation(
        summary = "유저 권한 부여",
        description = """
            지정한 유저의 권한을 변경합니다.  
            [필요 권한] SUPER_ADMIN
            """
    )
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @PostMapping("/roles")
    public ApiResponse<UpdateUserRoleResponse> updateRole(@RequestBody @Valid UpdateUserRoleRequest request) {
        UpdateUserRoleResponse response = userService.updateUserRole(request);
        return ApiResponse.ok(response);
    }
}

