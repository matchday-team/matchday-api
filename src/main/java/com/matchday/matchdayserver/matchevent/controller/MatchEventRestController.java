package com.matchday.matchdayserver.matchevent.controller;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.matchevent.service.MatchEventDeleteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "match-event", description = "매치 이벤트 관련 API")
@RequestMapping("/api/v1/match-event")
@RestController
@RequiredArgsConstructor
public class MatchEventRestController {

  private final MatchEventDeleteService matchEventDeleteService;

  @Operation(summary = "이벤트 전체 삭제" , description = "matchId 해당하는 matchEvent 전체삭제 <br>삭제 후 \"/topic/match-delete-events\" 채널로 Websocket 메시지 전송합니다")
  @DeleteMapping("/{matchId}")
  public ApiResponse<Long> deleteAllEvents(@PathVariable Long matchId) {
    return ApiResponse.ok(matchEventDeleteService.deleteAllEvents(matchId));
  }
}
