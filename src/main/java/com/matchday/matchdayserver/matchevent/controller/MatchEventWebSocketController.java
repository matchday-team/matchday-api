package com.matchday.matchdayserver.matchevent.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.matchday.matchdayserver.common.model.Message;
import com.matchday.matchdayserver.matchevent.model.dto.MatchEventRequest;
import com.matchday.matchdayserver.matchevent.service.MatchEventSaveService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MatchEventWebSocketController {

  private final MatchEventSaveService matchEventSaveService;

  @MessageMapping("/match/{matchId}")
  public void recordEvent(@DestinationVariable Long matchId, Message<MatchEventRequest> matchEventRequest) {
    matchEventSaveService.saveMatchEvent(matchId, matchEventRequest);
  }
}
