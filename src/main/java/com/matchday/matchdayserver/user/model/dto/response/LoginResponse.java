package com.matchday.matchdayserver.user.model.dto.response;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    String accessToken; //서버 access token
    Cookie refreshTokenCookie; //서버 refresh token 쿠키 변환값
    Long id;
}
