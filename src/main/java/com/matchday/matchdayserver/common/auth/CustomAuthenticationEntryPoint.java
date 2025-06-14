package com.matchday.matchdayserver.common.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchday.matchdayserver.common.response.ApiExceptionResponse;
import com.matchday.matchdayserver.common.response.JwtStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        String requestUri = request.getRequestURI();
        String clientIp = request.getRemoteAddr();
        String authorizationHeader = request.getHeader("Authorization");

        log.warn("인증 실패 - 요청 URI: {}, 클라이언트 IP: {}, Authorization 헤더 존재: {}, 예외 타입: {}, 메시지: {}",
                requestUri,
                clientIp,
                authorizationHeader != null,
                authException.getClass().getSimpleName(),
                authException.getMessage());

        response.setStatus(JwtStatus.UNAUTHORIZED.getHttpStatusCode());
        response.setContentType("application/json");

        String jsonResponse = objectMapper.writeValueAsString(
            ApiExceptionResponse.error(JwtStatus.UNAUTHORIZED));
        response.getWriter().write(jsonResponse);
    }
}
