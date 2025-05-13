package com.matchday.matchdayserver.common.response;

package com.matchday.matchdayserver.common.response;

import org.springframework.http.HttpStatus;

public enum AuthStatus implements StatusInterface {
    UNAUTHORIZED(401, 9000, "JWT 토큰 검증 실패, "),
    INVALID_AUTHORIZATION_HEADER(401, 9001, "올바른 Bearer 형식이 아님, Bearer(공백) 총 7자")

    ;

    private final int httpStatusCode;
    private final int customStatusCode;
    private String description;

    AuthStatus(int httpStatusCode, int customStatusCode, String description) {
        this.httpStatusCode = httpStatusCode;
        this.customStatusCode = customStatusCode;
        this.description = description;
    }

    public void setCustomDescription(String customDescription) {
        this.description = this.description + "\n" + customDescription;
    }

    @Override
    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    @Override
    public int getCustomStatusCode() {
        return this.customStatusCode;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
