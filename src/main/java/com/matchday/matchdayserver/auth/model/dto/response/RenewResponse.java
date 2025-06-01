package com.matchday.matchdayserver.auth.model.dto.response;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class RenewResponse {
    private final String accessToken; //재발급 받은 서버 access token
    private final String refreshToken; //재발급 받은 서버 refresh token
}
