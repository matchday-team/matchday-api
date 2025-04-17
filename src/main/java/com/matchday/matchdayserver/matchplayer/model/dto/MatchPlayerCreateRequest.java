package com.matchday.matchdayserver.matchplayer.model.dto;

import com.matchday.matchdayserver.matchplayer.model.entity.MatchPlayer;
import lombok.Getter;

@Getter
public class MatchPlayerCreateRequest {
    private Long userId;
    private MatchPlayer.Role role;
    private String match_position;
}
