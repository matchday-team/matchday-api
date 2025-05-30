package com.matchday.matchdayserver.auth.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RenewResponse {
    String accessToken; //재발급 받은 서버 access token
}
