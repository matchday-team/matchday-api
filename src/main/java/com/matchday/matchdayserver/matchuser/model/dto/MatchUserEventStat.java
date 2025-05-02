package com.matchday.matchdayserver.matchuser.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchUserEventStat {
    //MatchEventQueryService에서 MatchUserService로 선수별 주요 이벤트 통계 데이터 전달 시 사용합니다
    private Integer goals;
    private Integer assists;
    private Integer cards;

    @Builder
    public MatchUserEventStat(Integer goals, Integer assists, Integer cards) {
        this.goals = goals;
        this.assists = assists;
        this.cards = cards;
    }
}