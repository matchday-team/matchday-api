package com.matchday.matchdayserver.match.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class MatchInfoResponse {
  @Schema(description = "id", example = "매치 id")
  private Long id;

  //장소
  @Schema(description = "장소", example = "경기 장소")
  private String stadium;
  //날짜
  @Schema(description = "날짜")
  private LocalDate matchDate;

  //시작 시간
  @Schema(description = "시작 시간")
  private LocalTime startTime;

  //종료 시간
  @Schema(description = "시작 시간")
  private LocalTime endTime;

  //주심
  @Schema(description = "주심", example = "주심")
  private String mainRefereeName;

  //부심
  @Schema(description = "부심1", example = "부심1")
  private String assistantReferee1;

  //부심2
  @Schema(description = "부심2", example = "부심2")
  private String assistantReferee2;

  //대기심
  @Schema(description = "대기심", example = "대기심")
  private String fourthReferee;

  @Schema(description = "전반 시작 시간")
  private LocalTime firstHalfStartTime;

  @Schema(description = "후반 시작 시간")
  private LocalTime secondHalfStartTime;

  @Builder
  public MatchInfoResponse(Long id, String stadium, LocalDate matchDate, LocalTime startTime,
      LocalTime endTime, String mainRefereeName, String assistantReferee1,
      String assistantReferee2, String fourthReferee,
      LocalTime firstHalfStartTime, LocalTime secondHalfStartTime) {
    this.id = id;
    this.stadium = stadium;
    this.matchDate = matchDate;
    this.startTime = startTime;
    this.endTime = endTime;
    this.mainRefereeName = mainRefereeName;
    this.assistantReferee1 = assistantReferee1;
    this.assistantReferee2 = assistantReferee2;
    this.fourthReferee = fourthReferee;
    this.firstHalfStartTime = firstHalfStartTime;
    this.secondHalfStartTime = secondHalfStartTime;
  }
}
