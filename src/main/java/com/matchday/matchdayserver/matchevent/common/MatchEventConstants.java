package com.matchday.matchdayserver.matchevent.common;

public class MatchEventConstants {
    public static final String MATCH_EVENT_URL_PREFIX = "/topic/match/";

    public static String getMatchEventUrl(Long matchId) {
        return MATCH_EVENT_URL_PREFIX + matchId;
    }
}
