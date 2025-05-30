package com.matchday.matchdayserver.auth.service;

import com.matchday.matchdayserver.auth.model.dto.enums.JwtTokenType;
import com.matchday.matchdayserver.auth.model.dto.request.OauthLoginRequest;
import com.matchday.matchdayserver.auth.model.dto.response.GoogleAccessTokenResponse;
import com.matchday.matchdayserver.auth.model.dto.response.OauthLoginResponse;
import com.matchday.matchdayserver.auth.model.dto.response.GoogleProfileResponse;
import com.matchday.matchdayserver.common.auth.JwtTokenProvider;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.model.enums.SocialType;
import com.matchday.matchdayserver.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOauthService {
    @Value("${oauth.google.client-id}")
    private String googleClientId;

    @Value("${oauth.google.client-secret}")
    private String googleClientSecret;

    @Value("${oauth.google.redirect-url}")
    private String googleRedirectUri;

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Google OAuth 로그인 처리
     * 1. 인가 코드로 Google Access Token 요청
     * 2. Google Access Token으로 사용자 정보 조회
     * 3. 기존 유저 조회 or 신규 유저 생성
     * 4. JWT 토큰 발급 및 반환 <- 이것이 스프링 서버 Access Token
     */
    public OauthLoginResponse googleLogin(OauthLoginRequest oAuthLoginRequest) {
        GoogleAccessTokenResponse googleAccessToken = getAccessToken(oAuthLoginRequest.getCode());
        GoogleProfileResponse googleProfile = getProfile(googleAccessToken.getAccess_token());
        //유저 가져오기
        User user = userRepository.findBySocialId(googleProfile.getSub());
        //없으면 만들기
        if (user == null) {
            user = createOatuh(googleProfile, SocialType.GOOGLE);
        }
        //토큰발급
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", user.getId());
        payload.put("role", user.getRole());

        String jwtAccessToken=jwtTokenProvider.createToken(user.getEmail(), payload, JwtTokenType.ACCESS);
        String jwtRefreshToken=jwtTokenProvider.createToken(user.getEmail(), payload, JwtTokenType.REFRESH);

        return new OauthLoginResponse(jwtAccessToken, createCookie("refreshToken",jwtRefreshToken), user.getId());
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setSecure(false); //https (운영환경에선)false
        cookie.setPath("/"); //전체 경로에 대해 쿠키 유효
        cookie.setHttpOnly(true);

        return cookie;
    }

    /**
     * Google 서버에 인가 코드를 전달하여 Access Token 발급
     */
    private GoogleAccessTokenResponse getAccessToken(String authorizationCode){
        RestClient restClient = RestClient.create();

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("grant_type", "authorization_code");

        ResponseEntity<GoogleAccessTokenResponse> response= restClient.post()
            .uri("https://oauth2.googleapis.com/token")
            .header("Content-Type", "application/x-www-form-urlencoded")//form data 형식으로 보낸다
            .body(params)
            .retrieve()//retrieve:응답 body값만을 추출
            .toEntity(GoogleAccessTokenResponse.class);// JSON → AccessTokenDto 역직렬화(restClient 의 기능)

        return response.getBody();
    }

    /**
     * Access Token을 사용하여 Google 사용자 프로필 조회
     */
    private GoogleProfileResponse getProfile(String accessToken) {
        RestClient restClient = RestClient.create();

        ResponseEntity<GoogleProfileResponse> response = restClient.get()
            .uri("https://openidconnect.googleapis.com/v1/userinfo")
            .header("Authorization", "Bearer" + accessToken)
            .retrieve()
            .toEntity(GoogleProfileResponse.class);

        return response.getBody();
    }

    /**
     * Google 사용자 정보를 기반으로 신규 유저 생성
     */
    private User createOatuh(GoogleProfileResponse googleProfile,SocialType socialType) {
        User user = User.builder().
            name(googleProfile.getName()).
            socialId(googleProfile.getSub()).
            email(googleProfile.getEmail()).
            socialType(socialType).
            build();
        return userRepository.save(user);
    }
}
