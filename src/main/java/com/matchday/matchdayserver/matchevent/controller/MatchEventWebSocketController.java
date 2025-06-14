package com.matchday.matchdayserver.matchevent.controller;

import com.matchday.matchdayserver.matchevent.model.dto.MatchEventCancelRequest;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventRequest;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventUserRequest;
import com.matchday.matchdayserver.matchevent.service.MatchEventCancelService;
import com.matchday.matchdayserver.matchevent.service.MatchEventSaveService;
import com.matchday.matchdayserver.common.websocket.resolver.WebSocketUserId;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MatchEventWebSocketController {

    private final MatchEventSaveService matchEventSaveService;
    private final MatchEventCancelService matchEventCancelService;

    @MessageMapping("/match/{matchId}")
    public void recordEvent(@DestinationVariable Long matchId,
                           @WebSocketUserId Long userId,
                           MatchEventUserRequest matchEventRequest) {
        
        log.info("Recording event for match: {}, user: {}", matchId, userId);
        // userId는 JWT 토큰에서 자동으로 추출되어 주입됩니다
        matchEventSaveService.saveMatchEvent(matchId, matchEventRequest);
    }

    @MessageMapping("/match/{matchId}/teams/{teamId}")
    public void recordTeamEvent(@DestinationVariable Long matchId,
                               @DestinationVariable Long teamId,
                               @WebSocketUserId Long userId,
                               MatchEventRequest matchEventRequest) {
        
        log.info("Recording team event for match: {}, team: {}, user: {}", matchId, teamId, userId);
        // userId는 JWT 토큰에서 자동으로 추출되어 주입됩니다
        matchEventSaveService.saveMatchTeamEvent(matchId, teamId, matchEventRequest);
    }

    @MessageMapping("/match/{matchId}/cancel")
    public void cancelEvent(@DestinationVariable Long matchId,
                           @WebSocketUserId Long userId,
                           MatchEventCancelRequest matchEventCancelRequest) {
        
        log.info("Canceling event for match: {}, user: {}", matchId, userId);
        // userId는 JWT 토큰에서 자동으로 추출되어 주입됩니다
        matchEventCancelService.cancelEvent(matchId, matchEventCancelRequest);
    }
}
