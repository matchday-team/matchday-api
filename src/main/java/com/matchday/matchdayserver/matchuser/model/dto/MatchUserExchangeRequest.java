package com.matchday.matchdayserver.matchuser.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchUserExchangeRequest {
    @NotNull
    @Schema(description = "교체할 선수의 매치 유저 아이디")
    private Long fromMatchUserId;

    @NotNull
    @Schema(description = "교체할 선수의 유저 아이디")
    private Long toMatchUserId;

    @Schema(description = "교체 사유 등", nullable = true)
    private String message;
}
