package com.matchday.matchdayserver.matchevent.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.matchday.matchdayserver.matchevent.model.dto.MatchEventRequest;
import com.matchday.matchdayserver.matchevent.service.MatchEventSaveService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MatchEventController {

  private final MatchEventSaveService matchEventSaveService;

  @MessageMapping("/game/{gameId}")
  public void recordEvent(@DestinationVariable String gameId, MatchEventRequest matchEventRequest) {
    matchEventSaveService.saveMatchEvent(matchEventRequest);
  }
}
