package com.matchday.matchdayserver.matchuser.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchUserEventStat {
    private Integer goals;
    private Integer ownGoals;
    private Integer assists;
    private Integer yellowCards;
    private Integer redCards;
    private Integer caution;   // == warning
    private boolean sentOff;   // 퇴장 여부

    @Builder
    public MatchUserEventStat(Integer goals, Integer ownGoals, Integer assists,
        Integer yellowCards, Integer redCards,
        Integer caution, boolean sentOff) {
        this.goals = goals;
        this.ownGoals = ownGoals;
        this.assists = assists;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.caution = caution;
        this.sentOff = sentOff;
    }
}
