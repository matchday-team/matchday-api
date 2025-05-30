package com.matchday.matchdayserver.auth.model.dto.response;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OauthLoginResponse {
    String accessToken; //서버 access token
    Cookie refreshTokenCookie; //서버 refresh token 쿠키 변환값
    Long id;
}
