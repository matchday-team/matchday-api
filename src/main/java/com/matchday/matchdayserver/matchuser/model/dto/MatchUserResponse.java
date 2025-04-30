package com.matchday.matchdayserver.matchuser.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchUserResponse {

  private Long id; //선수의 아이디
  private String name; //이름
  //축구 선수에게만 필요한 필드
  private Integer number; //등번호
  private String matchPosition; // 주의: defaultPosition 과 matchPostition은 다름
  private String matchGrid;

  private Integer goals;        // 누적 득점
  private Integer assists;      // 누적 어시스트
  private Integer fouls;        // 누적 파울

  @Builder
  public MatchUserResponse(Long id,String name,Integer number, String matchPosition, String matchGrid, Integer goals, Integer assists, Integer fouls) {
    this.id = id;
    this.name = name;
    this.number = number;
    this.matchPosition = matchPosition;
    this.matchGrid = matchGrid;
    this.goals = goals;
    this.assists = assists;
    this.fouls = fouls;
  }
}
