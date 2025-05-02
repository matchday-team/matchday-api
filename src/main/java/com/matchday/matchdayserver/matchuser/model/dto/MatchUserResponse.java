package com.matchday.matchdayserver.matchuser.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchUserResponse {

    private Long id;
    private String name; //이름
    //축구 선수에게만 필요한 필드
    private Integer number; //등번호
    private String matchPosition; // 주의: defaultPosition 과 matchPostition은 다름
    private Integer goals;        // 누적 득점
    private Integer assists;      // 누적 어시스트
    private Integer cards;        // 누적 카드 개수 (엘로,레드 합산)
}