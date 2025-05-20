package com.matchday.matchdayserver.matchevent.common;

public class MatchEventConstants {
    public static final String MATCH_URL_PREFIX  = "/topic/match/";

    public static String getMatchEventUrl(Long matchId) {
        return MATCH_URL_PREFIX  + matchId;
    }

    public static String getMemoTopic(Long matchId) { return MATCH_URL_PREFIX  + matchId + "/memo"; }
}
