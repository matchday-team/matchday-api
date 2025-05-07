package com.matchday.matchdayserver.team.model.dto.response;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class TeamMemberResponse {
    private Long id;
    private String name;
    private Integer number;
    private String defaultPosition;
    private Boolean isActive; //현재 팀에서 활동 여부
    private String imageUrl; //프로필 이미지 url

    @Builder
    public TeamMemberResponse(Long id, String name, Integer number, String defaultPosition, Boolean isActive,String imageUrl) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.defaultPosition = defaultPosition;
        this.isActive = isActive;
        this.imageUrl = imageUrl;
    }
}
