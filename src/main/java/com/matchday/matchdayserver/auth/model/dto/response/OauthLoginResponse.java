package com.matchday.matchdayserver.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OauthLoginResponse {
    String accessToken; //서버 access token
    Long id;
}
