package com.matchday.matchdayserver.matchevent.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "경기 이벤트 타입")
public enum MatchEventType {
    @Schema(description = "골")
    GOAL("골"),

    @Schema(description = "어시스트")
    ASSIST("어시스트"),

    @Schema(description = "슛")
    SHOT("슛"),

    @Schema(description = "코너킥")
    CORNER_KICK("코너킥"),

    @Schema(description = "유효슛")
    VALID_SHOT("유효슛"),

    @Schema(description = "파울")
    FOUL("파울"),

    @Schema(description = "오프사이드")
    OFFSIDE("오프사이드"),

    @Schema(description = "교체입장")
    SUB_IN("교체입장"),

    @Schema(description = "교체퇴장")
    SUB_OUT("교체퇴장"),

    @Schema(description = "옐로카드")
    YELLOW_CARD("옐로카드"),

    @Schema(description = "레드카드(퇴장)")
    RED_CARD("퇴장"),

    @Schema(description = "자책골")
    OWN_GOAL("자책골"),

    @Schema(description = "경고")
    WARNING("경고"),
    ;

    public final String value;

    MatchEventType(String value) {
        this.value = value;
    }
}
