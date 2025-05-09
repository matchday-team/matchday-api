package com.matchday.matchdayserver.matchevent.model.dto;

import com.matchday.matchdayserver.matchevent.model.enums.MatchEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "경기 이벤트 삭제 완료 응답 DTO")
public class MatchEventDeleteResponse {
    @NotNull
    @Schema(description = "이벤트들이 삭제된 경기의 ID", example = "1")
    private Long id;
}
