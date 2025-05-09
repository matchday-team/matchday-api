package com.matchday.matchdayserver.s3.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "프로필 사진 등록/조회 URL 응답")
public class S3PresignedResponse {

    @NotNull
    @Schema(description = "프로필 등록/조회 URL")
    private final String url;
}
