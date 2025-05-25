package com.matchday.matchdayserver.auth.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) //Google API가 응답 형식을 변경할 가능성에 대비해 필요한 json 필드만 받아오도록 없는 필드는 자동무시
public class GoogleAccessTokenResponse {
    private String access_token;
    private String expires_in;
    private String scope;
    private String id_token;
}
