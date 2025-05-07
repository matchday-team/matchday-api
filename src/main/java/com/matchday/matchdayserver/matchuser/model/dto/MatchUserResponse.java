package com.matchday.matchdayserver.matchuser.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "매치 참가자 통계 응답")
public class MatchUserResponse {

    @Schema(description = "유저 ID", example = "12")
    private Long id;

    @Schema(description = "유저 이름", example = "홍길동")
    private String name;

    @Schema(description = "등번호", example = "10")
    private Integer number;

    @Schema(description = "매치 포지션", example = "MF")
    private String matchPosition;

    @Schema(description = "선수 그리드 좌표", example = "1")
    private Integer matchGrid;

    @Schema(description = "득점 수", example = "2")
    private Integer goals;

    @Schema(description = "어시스트 수", example = "1")
    private Integer assists;

    @Schema(description = "옐로카드 수", example = "1")
    private Integer yellowCards;

    @Schema(description = "레드카드 수", example = "0")
    private Integer redCards;

    @Schema(description = "경고 누적 수 (옐로카드 개수)", example = "1")
    private Integer caution;

    @Schema(description = "퇴장 여부 (옐로 2장 이상 또는 레드 1장 이상)", example = "false")
    private boolean sentOff;

    @Builder
    public MatchUserResponse(Long id, String name, Integer number, String matchPosition, Integer matchGrid,
        Integer goals, Integer assists,
        Integer yellowCards, Integer redCards, Integer caution, boolean sentOff) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.matchPosition = matchPosition;
        this.matchGrid = matchGrid;
        this.goals = goals;
        this.assists = assists;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.caution = caution;
        this.sentOff = sentOff;
    }
}
