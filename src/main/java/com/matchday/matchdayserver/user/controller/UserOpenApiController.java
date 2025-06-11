package com.matchday.matchdayserver.user.controller;

import com.matchday.matchdayserver.auth.model.dto.request.OauthLoginRequest;
import com.matchday.matchdayserver.auth.model.dto.response.OauthLoginResponse;
import com.matchday.matchdayserver.auth.model.dto.response.RenewResponse;
import com.matchday.matchdayserver.auth.model.dto.response.TokenRenewResponse;
import com.matchday.matchdayserver.auth.service.GoogleOauthService;
import com.matchday.matchdayserver.auth.service.KakaoOauthService;
import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.user.model.dto.LoginUserDto;
import com.matchday.matchdayserver.user.model.dto.response.LoginResponse;
import com.matchday.matchdayserver.user.service.UserOpenApiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "open-api", description = "인증이 필요없는 API")
@RequestMapping("/open-api/v1/users")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserOpenApiController {

    private final GoogleOauthService googleOauthService;
    private final KakaoOauthService kakaoOauthService;
    private final UserOpenApiService userOpenApiService;

    @PostMapping("/google")
    public ApiResponse<OauthLoginResponse> googleLogin(@RequestBody OauthLoginRequest oAuthLoginRequest,HttpServletResponse response){
        //유저 정보 가져오기
        LoginUserDto loginUserDto = googleOauthService.googleLogin(oAuthLoginRequest);
        //유저 정보로 엑세스토큰과 리프레시 토큰 받아오기
        LoginResponse loginResponse = userOpenApiService.doLogin(loginUserDto);
        //쿠키 세팅
        response.addCookie(userOpenApiService.createRefreshCookie(loginResponse.getRefreshToken()));
        //엑세스 토큰 리턴
        return ApiResponse.ok(new OauthLoginResponse(loginResponse.getAccessToken()));
    }

    @PostMapping("/kakao")
    public ApiResponse<OauthLoginResponse> kakaoLogin(@RequestBody OauthLoginRequest oAuthLoginRequest,HttpServletResponse response){
        //유저 정보 가져오기
        LoginUserDto loginUserDto = kakaoOauthService.kakaoLogin(oAuthLoginRequest);
        //유저 정보로 엑세스토큰과 리프레시 토큰 받아오기
        LoginResponse loginResponse = userOpenApiService.doLogin(loginUserDto);
        //쿠키 세팅
        response.addCookie(userOpenApiService.createRefreshCookie(loginResponse.getRefreshToken()));
        //엑세스 토큰 리턴
        return ApiResponse.ok(new OauthLoginResponse(loginResponse.getAccessToken()));
    }

    @PostMapping("/renew")
    public ApiResponse<TokenRenewResponse> renew(HttpServletRequest request,HttpServletResponse response){
        RenewResponse renewResponse=userOpenApiService.renewToken(request);
        response.addCookie(userOpenApiService.createRefreshCookie(renewResponse.getRefreshToken()));
        return ApiResponse.ok(new TokenRenewResponse(renewResponse.getAccessToken()));
    }
}
