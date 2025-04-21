package com.matchday.matchdayserver.team.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TeamSearchResponse { //팀명 검색시 사용
    private Long id;
    private String name;

    @Builder
    public TeamSearchResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}