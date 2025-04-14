package com.matchday.matchdayserver.team.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamBasicDto { //팀명 검색시 사용
    private Long id;
    private String name;
}