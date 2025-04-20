package com.matchday.matchdayserver.matchuser.model.dto;

import com.matchday.matchdayserver.matchuser.model.entity.MatchUser;
import lombok.Getter;

@Getter
public class MatchUserCreateRequest {
    private Long userId;
    private MatchUser.Role role;
    private String match_position;
    private String match_grid;
}
