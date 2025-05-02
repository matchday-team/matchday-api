package com.matchday.matchdayserver.matchuser.controller;

import com.matchday.matchdayserver.common.model.Message;
import com.matchday.matchdayserver.matchuser.model.dto.MatchUserExchangeRequest;
import com.matchday.matchdayserver.matchuser.service.MatchUserExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MatchUserExchangeController {

    private final MatchUserExchangeService matchUserExchangeService;

    @MessageMapping("/match/{matchId}/exchange")
    public void exchangePlayer(@DestinationVariable Long matchId,
        Message<MatchUserExchangeRequest> matchUserExchangeRequest) {
        matchUserExchangeService.exchangeMatchUser(matchId, matchUserExchangeRequest);
    }
}
