package com.matchday.matchdayserver.auth.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OauthLoginRequest {
    private String code;
    //여기에 프론트에서 폼 등으로 입력받은 추가데이터 받을 수 있음
    //ex 선호 팀,나이 등 oauth 회원가입
}
