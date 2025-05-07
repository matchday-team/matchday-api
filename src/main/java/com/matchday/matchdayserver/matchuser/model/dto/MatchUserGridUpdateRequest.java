package com.matchday.matchdayserver.matchuser.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MatchUserGridUpdateRequest {
    @NotNull
    @Min(0)
    @Max(29)
    @Schema(description = "그리드 X 좌표", example = "1")
    private int matchGridX;

    @NotNull
    @Min(0)
    @Max(29)
    @Schema(description = "그리드 X 좌표", example = "2")
    private int matchGridY;
}
