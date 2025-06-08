package com.matchday.matchdayserver.auth.service;

import com.matchday.matchdayserver.auth.mapper.UserMapper;
import com.matchday.matchdayserver.auth.model.dto.enums.JwtTokenType;
import com.matchday.matchdayserver.auth.model.dto.request.OauthLoginRequest;
import com.matchday.matchdayserver.auth.model.dto.response.GoogleAccessTokenResponse;
import com.matchday.matchdayserver.auth.model.dto.response.OauthLoginResponse;
import com.matchday.matchdayserver.auth.model.dto.response.GoogleProfileResponse;
import com.matchday.matchdayserver.common.auth.JwtTokenProvider;
import com.matchday.matchdayserver.user.model.dto.LoginUserDto;
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

    /**
     * REST API 방식 Google OAuth 로그인 처리
     * 1. 인가 코드로 Google Access Token 요청
     * 2. Google Access Token으로 사용자 정보 조회
     * 3. 기존 유저 조회 or 신규 유저 생성
     * 4. User 객체 반환
     */
    public LoginUserDto googleLogin(OauthLoginRequest oAuthLoginRequest) {
        // 인가 코드로 Google Access Token 요청
        GoogleAccessTokenResponse googleAccessToken = getAccessToken(oAuthLoginRequest.getCode());
        // Google Access Token으로 사용자 정보 조회
        GoogleProfileResponse googleProfile = getProfile(googleAccessToken.getAccess_token());
        // 기존 유저 조회 or 신규 유저 생성
        User user = userRepository.findBySocialId(googleProfile.getSub());
        if (user == null) {
            user = createOatuh(googleProfile, SocialType.GOOGLE);
        }
        return UserMapper.toLoginUserDto(user);
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
     * 구글 Access Token을 사용하여 Google 사용자 프로필 조회
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
