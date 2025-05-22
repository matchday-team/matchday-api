package com.matchday.matchdayserver.match.util;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.MatchStatus;
import com.matchday.matchdayserver.match.model.entity.Match;
import com.matchday.matchdayserver.match.model.enums.MatchState;

public class MatchStateValidator {

    private MatchStateValidator() {} // 유틸클래스니까

    public static void validateInPlay(Match match) {
        MatchState state = match.getMatchState();
        if (!(state == MatchState.PLAY_FIRST_HALF || state == MatchState.PLAY_SECOND_HALF)) {
            throw new ApiException(MatchStatus.NOT_IN_PLAY_MATCH);
        }
    }
}