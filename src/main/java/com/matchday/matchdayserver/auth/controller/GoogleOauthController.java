package com.matchday.matchdayserver.auth.controller;

import com.matchday.matchdayserver.auth.model.dto.request.OauthLoginRequest;
import com.matchday.matchdayserver.auth.model.dto.response.OauthLoginResponse;
import com.matchday.matchdayserver.auth.service.GoogleOauthService;
import com.matchday.matchdayserver.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "auth", description = "인증 관련 API")
@RestController
@RequestMapping("/api/v1/auth/oauth")
@RequiredArgsConstructor
public class GoogleOauthController {

    private final GoogleOauthService googleOauthService;

    @PostMapping("/google")
    public ApiResponse<OauthLoginResponse> googleLogin(@RequestBody OauthLoginRequest oAuthLoginRequest){
        return ApiResponse.ok(googleOauthService.googleLogin(oAuthLoginRequest));
    }
}
