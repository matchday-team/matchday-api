package com.matchday.matchdayserver.match.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class MatchMemoRequest {
    @Schema(description = "매치 메모", nullable = true)
    private String memo;
}
