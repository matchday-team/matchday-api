package com.matchday.matchdayserver.user.service;

import com.matchday.matchdayserver.auth.entity.RefreshToken;
import com.matchday.matchdayserver.auth.mapper.UserMapper;
import com.matchday.matchdayserver.auth.model.dto.enums.JwtTokenType;
import com.matchday.matchdayserver.auth.model.dto.response.RenewResponse;
import com.matchday.matchdayserver.auth.repository.RefreshTokenRepository;
import com.matchday.matchdayserver.auth.service.GoogleOauthService;
import com.matchday.matchdayserver.common.auth.JwtTokenProvider;
import com.matchday.matchdayserver.common.auth.TokenHelper;
import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.JwtStatus;
import com.matchday.matchdayserver.user.model.dto.LoginUserDto;
import com.matchday.matchdayserver.user.model.dto.response.LoginResponse;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserOpenApiService {

    private final TokenHelper tokenHelper;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public LoginResponse doLogin(LoginUserDto userLoginDto) {

        String accessToken=jwtTokenProvider.createToken(userLoginDto, JwtTokenType.ACCESS);
        String refreshToken=jwtTokenProvider.createToken(userLoginDto, JwtTokenType.REFRESH);

        refreshTokenRepository.save(RefreshToken.builder().
                userId(userLoginDto.getId()).
                token(accessToken).
                build());

        return new LoginResponse(accessToken,refreshToken);
    }

    public RenewResponse renewToken(HttpServletRequest request){
        String refreshToken = getTokenFromRequest(request);
        tokenHelper.validateToken(refreshToken, JwtTokenType.REFRESH);
        Claims claims = tokenHelper.getClaims(refreshToken);


        Long userId = Long.valueOf(claims.get("userId").toString());

        RefreshToken stored = refreshTokenRepository.findById(userId)
                .orElseThrow(() -> new ApiException(JwtStatus.NOTFOUND_TOKEN_IN_DB));

        if (!stored.getToken().equals(refreshToken)) {
            throw new ApiException(JwtStatus.INVALID_REFRESH_TOKEN);
        }

        //DB 조회로 사용자 상태 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(JwtStatus.NOTFOUND_USER));

        // 토큰 재발급
        String newAccessToken=jwtTokenProvider.createToken(UserMapper.toLoginUserDto(user), JwtTokenType.ACCESS);
        String newRefreshToken = jwtTokenProvider.createToken(UserMapper.toLoginUserDto(user), JwtTokenType.REFRESH);

        stored.update(newRefreshToken);
        refreshTokenRepository.save(stored);

        return new RenewResponse(newAccessToken,newRefreshToken);
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

    public Cookie createRefreshCookie(String value) {

        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(false); //https (운영환경에선)false
        cookie.setPath("/"); //전체 경로에 대해 쿠키 유효
        cookie.setHttpOnly(true);

        return cookie;
    }
}
