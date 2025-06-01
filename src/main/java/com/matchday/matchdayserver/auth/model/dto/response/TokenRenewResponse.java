package com.matchday.matchdayserver.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenRenewResponse {
    private final String accessToken;
}