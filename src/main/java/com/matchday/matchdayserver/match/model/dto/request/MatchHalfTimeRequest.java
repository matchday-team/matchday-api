package com.matchday.matchdayserver.match.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class MatchHalfTimeRequest {
    @Schema(description = "시작 시간")
    private LocalTime startTime;

    @Schema(description = "종료 시간")
    private LocalTime endTime;
}
