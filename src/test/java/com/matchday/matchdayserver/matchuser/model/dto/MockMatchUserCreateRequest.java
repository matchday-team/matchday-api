package com.matchday.matchdayserver.matchuser.model.dto;

import com.matchday.matchdayserver.matchuser.model.enums.MatchUserRole;

public class MockMatchUserCreateRequest {

    public static MatchUserCreateRequest create(Long userId, Long teamId) {
        return new MatchUserCreateRequest(
            userId,
            teamId,
            MatchUserRole.START_PLAYER,
            "FW",
            1
        );
    }

        public static MatchUserCreateRequest createArchives(Long userId, Long teamId) {
        return new MatchUserCreateRequest(
            userId,
            teamId,
            MatchUserRole.ARCHIVES,
            "FW",
            1
        );
    }
}