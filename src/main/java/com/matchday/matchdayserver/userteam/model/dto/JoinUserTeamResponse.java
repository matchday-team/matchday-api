package com.matchday.matchdayserver.userteam.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class JoinUserTeamResponse { //팀 입단 성공 후 클라이언트에게 보낼 응답값
    private Long id;
    private String teamName;
    private String userName;
    private Integer number;
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
