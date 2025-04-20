package com.matchday.matchdayserver.matchevent.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "경기 이벤트 타입")
public enum MatchEventType {
    @Schema(description = "골")
    GOAL,
    
    @Schema(description = "어시스트")
    ASSIST,
    
    @Schema(description = "슛")
    SHOT,
    
    @Schema(description = "유효슛")
    VALID_SHOT,
    
    @Schema(description = "파울")
    FOUL,
    
    @Schema(description = "오프사이드")
    OFFSIDE,
    
    @Schema(description = "교체입장")
    SUB_IN,
    
    @Schema(description = "교체퇴장")
    SUB_OUT,
    
    @Schema(description = "옐로카드")
    YELLOW_CARD,
    
    @Schema(description = "레드카드(퇴장)")
    RED_CARD,
    
    @Schema(description = "자책골")
    OWN_GOAL
    //골,어시스트,슛,유효슛,파울,오프사이드
    //교체입장,교체퇴장,옐로카드,레드카드(퇴장),자책골
}
