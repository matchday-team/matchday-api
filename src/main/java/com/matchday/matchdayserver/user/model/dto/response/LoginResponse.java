package com.matchday.matchdayserver.user.model.dto.response;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class LoginResponse {
    String accessToken; //서버 access token
    String refreshToken; //서버 refresh token 쿠키 변환값
}
