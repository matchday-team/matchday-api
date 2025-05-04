package com.matchday.matchdayserver.matchuser.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MatchUserGroupResponse {
    private List<MatchUserResponse> homeTeam;
    private List<MatchUserResponse> awayTeam;
}