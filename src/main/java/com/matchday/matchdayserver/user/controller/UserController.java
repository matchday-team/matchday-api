package com.matchday.matchdayserver.user.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.s3.service.S3PresignedService;
import com.matchday.matchdayserver.s3.dto.S3PresignedResponse;
import com.matchday.matchdayserver.user.model.dto.request.UserCreateRequest;
import com.matchday.matchdayserver.user.model.dto.request.UserJoinTeamRequest;
import com.matchday.matchdayserver.user.model.dto.response.UserInfoResponse;
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
    private final S3PresignedService s3PresignedManager;
    private static final String FOLDER_NAME = "users";

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
  @Operation(summary = "유저 프로필 업로드 presigned URL 발급", description = "userid와 확장자(jpg, jpeg, png, webp)를 넘겨주면 이미지 업로드용 URL과 저장할 FileName(파일명)을 반환합니다. <br> 반환 받은 파일명을 저장하세요(이미지 조회시 필요)")
  @GetMapping("/{userId}/profile/upload-url")
  public ApiResponse<S3PresignedResponse> generateUploadUrl(
      @PathVariable Long userId,
      @RequestParam String extension
  ) {
    return ApiResponse.ok(s3PresignedManager.generateUploadUrl(FOLDER_NAME, userId, extension));
  }

  @Operation(summary = "유저 프로필 이미지 read presigned URL 조회", description = "user 프로필 업로드에서 반환 받은 파일명(key)을 넘겨주면 이미지 read용 URL을 반환합니다")
  @GetMapping("/{userId}/profile/read-url")
  public ApiResponse<S3PresignedResponse> getProfileReadUrl(
      @PathVariable Long userId,
      @RequestParam String key
  ) {
    return ApiResponse.ok(s3PresignedManager.generateReadUrl(FOLDER_NAME, userId,key));
  }

  @Operation(summary = "유저 정보 조회", description = "유저 정보 조회 API입니다. <br> 특정 유저의 이름, 소속팀 id, 참여한 매치 id를 반환합니다. ")
  @GetMapping("/{userId}")
  public ApiResponse<UserInfoResponse> getUserInfo(@PathVariable Long userId){
    return ApiResponse.ok(userService.getUserInfo(userId));
  }
}
