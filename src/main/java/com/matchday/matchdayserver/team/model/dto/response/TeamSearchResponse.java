package com.matchday.matchdayserver.team.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "팀명 검색 응답 객체")
public class TeamSearchResponse { //팀명 검색시 사용

    @NotNull
    @Schema(description = "팀 ID")
    private Long id;

    @NotNull
    @Schema(description = "팀 이름")
    private String name;

    @Builder
    public TeamSearchResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}