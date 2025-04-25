package com.matchday.matchdayserver.team.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TeamResponse {
    private Long id;
    private String name;
    private String teamColor;

    @Builder
    public TeamResponse(Long id, String name, String teamColor) {
        this.id = id;
        this.name = name;
        this.teamColor = teamColor;
    }
}