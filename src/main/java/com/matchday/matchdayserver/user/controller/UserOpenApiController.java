package com.matchday.matchdayserver.user.controller;

import com.matchday.matchdayserver.auth.model.dto.request.OauthLoginRequest;
import com.matchday.matchdayserver.auth.model.dto.response.OauthLoginResponse;
import com.matchday.matchdayserver.auth.service.GoogleOauthService;
import com.matchday.matchdayserver.common.response.ApiResponse;
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


    @PostMapping("/google")
    public ApiResponse<OauthLoginResponse> googleLogin(@RequestBody OauthLoginRequest oAuthLoginRequest,HttpServletResponse response){
        OauthLoginResponse loginResponse = googleOauthService.googleLogin(oAuthLoginRequest);
        response.addCookie(loginResponse.getRefreshTokenCookie());

        return ApiResponse.ok(new OauthLoginResponse(
                loginResponse.getAccessToken(),
                null, // refreshToken은 쿠키로 보냈으니 null 처리
                loginResponse.getId()
        ));
    }

    @PostMapping("/renew")
    public ApiResponse<?> renewAccessToken(HttpServletRequest request, HttpServletResponse response){

        //리프레시 토큰 가져오기

        //리프레시 토큰 데이터베이스에 있는지 검증

        //리프레시 토큰이 만료되었는지 확인

        //JWT 만들어 리턴해주기
            return null;
    }



}
