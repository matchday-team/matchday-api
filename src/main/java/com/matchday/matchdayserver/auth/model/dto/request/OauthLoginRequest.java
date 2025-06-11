package com.matchday.matchdayserver.auth.model.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OauthLoginRequest {
    private String code;
    private String redirectUri; //
    //여기에 프론트에서 폼 등으로 입력받은 추가데이터 받을 수 있음
    //kakao,goole 로그인 둘다 이거로 쓰는중
    //ex 선호 팀,나이 등 oauth 회원가입
}
