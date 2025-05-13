package com.matchday.matchdayserver.match.model.dto.request;

import com.matchday.matchdayserver.match.model.enums.HalfType;
import com.matchday.matchdayserver.match.model.enums.TimeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class MatchHalfTimeRequest {

    @NotNull
    @Schema(description = "전/후반 ")
    private HalfType halfType;

    @NotNull
    @Schema(description = "시작/종료 시간")
    private TimeType timeType;

    @NotNull
    @Schema(description = "기록할 시간")
    private LocalTime time;
}
