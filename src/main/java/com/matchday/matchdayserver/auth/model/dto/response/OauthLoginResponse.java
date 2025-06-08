package com.matchday.matchdayserver.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class OauthLoginResponse {
    private final String accessToken; //서버 access token
}
