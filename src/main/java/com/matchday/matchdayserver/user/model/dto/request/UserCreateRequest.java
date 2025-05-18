package com.matchday.matchdayserver.user.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @NotBlank
    @Schema(description = "유저 이름", example = "홍길동")
    private String name;

    @Schema(description = "유저 프로필 이미지명", example = "users/1/597feb0b-0e44-48e6-aefdfdsfdsf.png", required = false, nullable = true)
    private String profileImg;
}
