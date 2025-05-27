package com.matchday.matchdayserver.common.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.warn("인가 실패, Authentication객체 있음: {}, 요청 URI: {}, 사용자: {}, 권한: {}",
                accessDeniedException.getMessage(),
                request.getRequestURI(),
                auth.getName(),
                auth.getAuthorities());
        } else {
            log.warn("인가 실패, Authentication객체 없음: {}, 요청 URI: {}, 인증 정보 없음",
                accessDeniedException.getMessage(),
                request.getRequestURI());
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"forbidden - access denied\"}");
    }

}
