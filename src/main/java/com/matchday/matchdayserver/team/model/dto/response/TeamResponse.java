package com.matchday.matchdayserver.team.model.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class TeamResponse {
    private Long id;
    private String name;
    private String teamColor;
    private String bottomColor;
    private String stockingColor;

    @Builder
    public TeamResponse(Long id, String name, String teamColor, String bottomColor, String stockingColor) {
        this.id = id;
        this.name = name;
        this.teamColor = teamColor;
        this.bottomColor = bottomColor;
        this.stockingColor = stockingColor;
    }
}