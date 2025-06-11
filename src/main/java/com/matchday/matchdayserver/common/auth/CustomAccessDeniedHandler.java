package com.matchday.matchdayserver.common.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchday.matchdayserver.common.exception.ApiExceptionInterface;
import com.matchday.matchdayserver.common.response.ApiExceptionResponse;
import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.common.response.AuthStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            log.warn("인가 실패, Authentication 객체 있음: {}, 요청 URI: {}, 사용자: {}, 권한: {}",
                accessDeniedException.getMessage(),
                request.getRequestURI(),
                auth.getName(),
                auth.getAuthorities());
        } else {
            log.warn("인가 실패, Authentication 객체 없음: {}, 요청 URI: {}", accessDeniedException.getMessage(), request.getRequestURI());
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        ApiExceptionResponse<ApiExceptionInterface> body = ApiExceptionResponse.error(AuthStatus.FORBIDDEN);

        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
