package com.matchday.matchdayserver.userteam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinUserTeamResponse { //팀 입단 성공 후 클라이언트에게 보낼 응답값
    private Long id;
    private String teamName;
    private String userName;
    private Integer number;
    private String defaultPosition;
}
