package com.matchday.matchdayserver.team.model.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeamCreateRequest {
    private String name;//팀명
    private String teamColor; //팀 컬러
}