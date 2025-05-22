package com.matchday.matchdayserver.matchuser.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "매치 참가자 통계 응답")
public class MatchUserResponse {

    @NotNull
    @Schema(description = "매치유저 ID", example = "5")
    private Long id;

    @NotNull
    @Schema(description = "유저 ID", example = "12")
    private Long userId;

    @NotNull
    @Schema(description = "유저 이름", example = "홍길동")
    private String name;

    @NotNull
    @Schema(description = "등번호", example = "10")
    private Integer number;

    @Schema(description = "매치 포지션", example = "MF", nullable = true)
    private String matchPosition;

    @Schema(description = "선수 그리드 좌표", example = "1", nullable = true)
    private Integer matchGrid;

    @NotNull
    @Schema(description = "득점 수", example = "2")
    private Integer goals;

    @NotNull
    @Schema(description = "자책골 수", example = "1")
    private Integer ownGoals;

    @NotNull
    @Schema(description = "어시스트 수", example = "1")
    private Integer assists;

    @NotNull
    @Schema(description = "옐로카드 수", example = "1")
    private Integer yellowCards;

    @NotNull
    @Schema(description = "레드카드 수", example = "0")
    private Integer redCards;

    @NotNull
    @Schema(description = "경고 누적 수 (옐로카드 개수)", example = "1")
    private Integer caution;

    @Schema(description = "퇴장 여부 (옐로 2장 이상 또는 레드 1장 이상)", example = "false")
    private boolean sentOff;

    @Schema(description = "프로필 이미지 url", example = "users/1/0f3578b1-c4e6-4f6f-2222-567f3f48648a.webp", nullable = true)
    private String profileImg;

    @Schema(description = "교체인 여부", example = "true")
    private boolean subIn;

    @Schema(description = "교체아웃 여부", example = "false")
    private boolean subOut;

    @Builder
    public MatchUserResponse(Long id, Long userId, String name, Integer number, String matchPosition, Integer matchGrid,
        Integer goals, Integer ownGoals, Integer assists,
        Integer yellowCards, Integer redCards, Integer caution, boolean sentOff, String profileImg,boolean subIn, boolean subOut) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.number = number;
        this.matchPosition = matchPosition;
        this.matchGrid = matchGrid;
        this.goals = goals;
        this.ownGoals = ownGoals;
        this.assists = assists;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.caution = caution;
        this.sentOff = sentOff;
        this.profileImg = profileImg;
        this.subIn = subIn;
        this.subOut = subOut;
    }
}
