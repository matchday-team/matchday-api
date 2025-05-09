package com.matchday.matchdayserver.userteam.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "팀 입단 정보 응답 객체")
public class JoinUserTeamResponse { //팀 입단 성공 후 클라이언트에게 보낼 응답값

    @NotNull
    @Schema(description = "UserTeam ID")
    private Long id;

    @NotNull
    @Schema(description = "팀 이름")
    private String teamName;

    @NotNull
    @Schema(description = "유저 이름")
    private String userName;

    @Schema(description = "넘버", nullable = true)
    private Integer number;

    @Schema(description = "고정 포지션", nullable = true)
    private String defaultPosition;

    @Builder
    public JoinUserTeamResponse(Long id, String teamName, String userName, Integer number, String defaultPosition) {
        this.id = id;
        this.teamName = teamName;
        this.userName = userName;
        this.number = number;
        this.defaultPosition = defaultPosition;
    }
}
