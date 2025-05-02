package com.matchday.matchdayserver.matchevent.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "경기 중 다양한 이벤트 발생 횟수 응답 객체")
public class ScoreResponse {

  @Schema(description = "골 횟수", example = "3")
  private int goalCount;

  @Schema(description = "슈팅 횟수", example = "10")
  private int shotCount;

  @Schema(description = "유효 슈팅 횟수", example = "5")
  private int validShotCount;

  @Schema(description = "코너킥 횟수", example = "7")
  private int cornerKickCount;

  @Schema(description = "오프사이드 횟수", example = "3")
  private int offsideCount;

  @Schema(description = "파울 횟수", example = "15")
  private int foulCount;

  @Schema(description = "경고 횟수", example = "2")
  private int warningCount;

  public void upGoalCount() {
    this.goalCount++;
  }

  public void upShotCount() {
    this.shotCount++;
  }

  public void upValidShotCount() {
    this.validShotCount++;
  }

  public void upCornerKickCount() {
    this.cornerKickCount++;
  }

  public void upOffsideCount() {
    this.offsideCount++;
  }

  public void upFoulCount() {
    this.foulCount++;
  }

  public void upWarningCount() {
    this.warningCount++;
  }

  public static ScoreResponse defaultScore() {
    return ScoreResponse.builder()
        .goalCount(0)
        .shotCount(0)
        .validShotCount(0)
        .cornerKickCount(0)
        .offsideCount(0)
        .foulCount(0)
        .warningCount(0)
        .build();
  }
}