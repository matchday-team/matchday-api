package com.matchday.matchdayserver.user.service;

import com.matchday.matchdayserver.auth.model.dto.enums.JwtTokenType;
import com.matchday.matchdayserver.auth.model.dto.response.RenewResponse;
import com.matchday.matchdayserver.auth.service.GoogleOauthService;
import com.matchday.matchdayserver.common.auth.JwtTokenProvider;
import com.matchday.matchdayserver.common.auth.TokenHelper;
import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.JwtStatus;
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

    public RenewResponse renewToken(HttpServletRequest request){
        String refreshToken = getTokenFromRequest(request);
        tokenHelper.validateToken(refreshToken, JwtTokenType.REFRESH);
        Claims claims = tokenHelper.getClaims(refreshToken);

        Map<String, Object> payload = new HashMap<>();
        Long userId = Long.valueOf(claims.get("userId").toString());
        String email = claims.getSubject();
        String role = claims.get("role").toString();
        payload.put("userId", userId);
        payload.put("role", role);

        //DB 조회로 사용자 상태 검증
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException(JwtStatus.NOTFOUND_USER));

        String jwtAccessToken=jwtTokenProvider.createToken(email, payload, JwtTokenType.ACCESS);

        return new RenewResponse(jwtAccessToken);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(GoogleOauthService.REFRESH_TOKEN_COOKIE_NAME)) {
                refresh = cookie.getValue();
            }
        }
        if (refresh == null) {
            throw new ApiException(JwtStatus.NOTFOUND_TOKEN_IN_COOKIE);
        }
        return refresh;
    }
}
