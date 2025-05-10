package com.matchday.matchdayserver.matchevent.controller;

import com.matchday.matchdayserver.matchevent.model.dto.MatchEventRequest;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventUserRequest;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.matchday.matchdayserver.common.model.Message;
import com.matchday.matchdayserver.matchevent.service.MatchEventSaveService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MatchEventWebSocketController {

    private final MatchEventSaveService matchEventSaveService;

    @MessageMapping("/match/{matchId}")
    public void recordEvent(@DestinationVariable Long matchId,
        Message<MatchEventUserRequest> matchEventRequest) {

        matchEventSaveService.saveMatchEvent(matchId, matchEventRequest);
    }

    @MessageMapping("/match/{matchId}/teams/{teamId}")
    public void recordTeamEvent(
        @DestinationVariable Long matchId,
        @DestinationVariable Long teamId,
        Message<MatchEventRequest> matchEventRequest) {

        matchEventSaveService.saveMatchTeamEvent(matchId, teamId, matchEventRequest);
    }
}
