package com.matchday.matchdayserver.matchuser.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MatchUserGridUpdateRequest {
    @NotNull
    @Min(value = 0, message = "0이상의 값이어야 합니다.")
    @Max(value = 29, message = "29 이하의 값이어야 합니다.")
    @Schema(description = "매치에서의 선수 그리드 좌표", example = "1")
    private Integer matchGrid;

}
