package com.matchday.matchdayserver.team.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamSearchListResponse {
    private List<TeamSearchResponse> teamSearchResponses;
}
