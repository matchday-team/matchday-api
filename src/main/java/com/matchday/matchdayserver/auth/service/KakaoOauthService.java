package com.matchday.matchdayserver.auth.service;

import com.matchday.matchdayserver.auth.mapper.UserMapper;
import com.matchday.matchdayserver.auth.model.dto.request.OauthLoginRequest;
import com.matchday.matchdayserver.auth.model.dto.response.KakaoAccessTokenResponse;
import com.matchday.matchdayserver.auth.model.dto.response.KakaoProfileResponse;
import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.AuthStatus;
import com.matchday.matchdayserver.user.model.dto.LoginUserDto;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.model.enums.SocialType;
import com.matchday.matchdayserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoOauthService {

    //얘네 아니면 필터링
    private static final Set<String> ALLOWED_REDIRECT_URIS = Set.of(
        "https://matchday-planner/login/kakao-redirect",
        "https://dev.matchday-planner/login/kakao-redirect",
        "https://localhost:5173/login/kakao-redirect",
        "http://localhost:3000/oauth/kakao/redirect"
    );

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;

//    @Value("${oauth.kakao.redirect-url}")
//    private String kakaoRedirectUri; 프론트에서 redirection-url 전달하는 로직이라 사용안함

    private final UserRepository userRepository;

    /**
     * REST API 방식 로그인 처리
     * 1. 인가 코드로 kakao Access Token 요청
     * 2. kakao Access Token으로 사용자 정보 조회
     * 3. 기존 유저 조회 or 신규 유저 생성
     * 4. User 객체 반환
     */
    public LoginUserDto kakaoLogin(OauthLoginRequest oAuthLoginRequest) {
        // 인가 코드로 Google Access Token 요청
        KakaoAccessTokenResponse kakaoAccessToken = getAccessToken(oAuthLoginRequest);
        // Google Access Token으로 사용자 정보 조회
        KakaoProfileResponse kakaoProfile = getKakaoProfile(kakaoAccessToken.getAccess_token());
        // 기존 유저 조회 or 신규 유저 생성
        User user = userRepository.findBySocialId(kakaoProfile.getId());
        if (user == null) {
            user = createOauth(kakaoProfile, SocialType.KAKAO);
        }
        return UserMapper.toLoginUserDto(user);
    }

    /**
     * kakao 서버에 인가 코드를 전달하여 Access Token 발급
     */
    private KakaoAccessTokenResponse getAccessToken(OauthLoginRequest oAuthLoginRequest){

        String authorizationCode = oAuthLoginRequest.getCode();
        String kakaoRedirectUri = oAuthLoginRequest.getRedirectUri();
        if (!ALLOWED_REDIRECT_URIS.contains(kakaoRedirectUri)) {
            throw new ApiException(AuthStatus.INVALID_REDIRECT_URI);
        }

        RestClient restClient = RestClient.create();

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", kakaoClientId);
        params.add("redirect_uri", kakaoRedirectUri);
        params.add("grant_type", "authorization_code");

        ResponseEntity<KakaoAccessTokenResponse> response= restClient.post()
            .uri("https://kauth.kakao.com/oauth/token")
            .header("Content-Type", "application/x-www-form-urlencoded")//form data 형식으로 보낸다
            .body(params)
            .retrieve()//retrieve:응답 body값만을 추출
            .toEntity(KakaoAccessTokenResponse.class);// JSON → AccessTokenDto 역직렬화(restClient 의 기능)
        return response.getBody();
    }

    /**
     * 구글 Access Token을 사용하여 Google 사용자 프로필 조회
     */
    private KakaoProfileResponse getKakaoProfile(String accessToken) {
        RestClient restClient = RestClient.create();

        ResponseEntity<KakaoProfileResponse> response = restClient.get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .header("Authorization", "Bearer " + accessToken)//"Bearer " 띄어쓰기 주의
            .retrieve()
            .toEntity(KakaoProfileResponse.class);
        return response.getBody();
    }

    /**
     * Google 사용자 정보를 기반으로 신규 유저 생성
     */
    private User createOauth(KakaoProfileResponse kakaoProfileResponse,SocialType socialType) {
        User user = User.builder().
            name(kakaoProfileResponse.getKakao_account().getProfile().getNickname()).
            socialId(kakaoProfileResponse.getId()).
            email(kakaoProfileResponse.getKakao_account().getEmail()).
            socialType(socialType).
            build();
        return userRepository.save(user);
    }
}
