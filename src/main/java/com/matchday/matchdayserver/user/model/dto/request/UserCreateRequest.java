package com.matchday.matchdayserver.user.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCreateRequest {

    @Schema(description = "유저 이름", example = "홍길동")
    private String name;

    @Schema(description = "유저 프로필 이미지명", example = "users/1/597feb0b-0e44-48e6-aefdfdsfdsf.png", required = false, nullable = true)
    private String profileImg;
}
