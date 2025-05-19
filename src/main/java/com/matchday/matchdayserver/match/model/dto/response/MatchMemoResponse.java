package com.matchday.matchdayserver.match.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "매치별 메모 응답 객체")
public class MatchMemoResponse {

    @Schema(description = "메모id")
    private Long matchId;

    @Schema(description = "메모", nullable = true)
    private String memo;
}