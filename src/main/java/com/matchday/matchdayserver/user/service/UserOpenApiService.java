package com.matchday.matchdayserver.user.service;

import com.matchday.matchdayserver.auth.model.dto.enums.JwtTokenType;
import com.matchday.matchdayserver.auth.model.dto.response.RenewResponse;
import com.matchday.matchdayserver.auth.service.GoogleOauthService;
import com.matchday.matchdayserver.common.auth.JwtTokenProvider;
import com.matchday.matchdayserver.common.auth.TokenHelper;
import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.JwtStatus;
import com.matchday.matchdayserver.user.model.dto.response.LoginResponse;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserOpenApiService {

    private final TokenHelper tokenHelper;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public LoginResponse doLogin(User user) {

        String accessToken=jwtTokenProvider.createToken(user, JwtTokenType.ACCESS);
        String refreshToken=jwtTokenProvider.createToken(user, JwtTokenType.REFRESH);
        return new LoginResponse(accessToken,createCookie(REFRESH_TOKEN_COOKIE_NAME,refreshToken),
            user.getId());
    }

    public RenewResponse renewToken(HttpServletRequest request){
        String refreshToken = getTokenFromRequest(request);
        tokenHelper.validateToken(refreshToken, JwtTokenType.REFRESH);
        Claims claims = tokenHelper.getClaims(refreshToken);

        //DB 조회로 사용자 상태 검증
        Long userId = Long.valueOf(claims.get("userId").toString());
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException(JwtStatus.NOTFOUND_USER));

        String jwtAccessToken=jwtTokenProvider.createToken(user, JwtTokenType.ACCESS);

        return new RenewResponse(jwtAccessToken);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new ApiException(JwtStatus.NOTFOUND_COOKIE);
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
                refresh = cookie.getValue();
            }
        }
        if (refresh == null) {
            throw new ApiException(JwtStatus.NOTFOUND_TOKEN_IN_COOKIE);
        }
        return refresh;
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(false); //https (운영환경에선)false
        cookie.setPath("/"); //전체 경로에 대해 쿠키 유효
        cookie.setHttpOnly(true);

        return cookie;
    }
}
