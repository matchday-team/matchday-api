package com.matchday.matchdayserver.user.controller;

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

@Tag(name = "open-api", description = "인증이 필요없는 API")
@RequestMapping("/open-api/v1/users")
@RestController
@RequiredArgsConstructor
public class UserOpenApiController {

    private final GoogleOauthService googleOauthService;

    @PostMapping("/google")
    public ApiResponse<OauthLoginResponse> googleLogin(@RequestBody OauthLoginRequest oAuthLoginRequest){
        return ApiResponse.ok(googleOauthService.googleLogin(oAuthLoginRequest));
    }

}
