package com.matchday.matchdayserver.common.auth;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.AuthStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenFilter extends GenericFilter {

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String token = httpServletRequest.getHeader("Authorization");

        try {
            if (token != null) {
                if (!token.startsWith("Bearer ")) {
                    throw new ApiException(AuthStatus.INVALID_AUTHORIZATION_HEADER);
                }

                String jwtToken = token.substring(7);
                Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();

                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));

                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, jwtToken, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            sendError(httpServletResponse, AuthStatus.EXPIRED_TOKEN, e);
        } catch (SignatureException e) {
            sendError(httpServletResponse, AuthStatus.INVALID_SIGNATURE, e);
        } catch (MalformedJwtException e) {
            sendError(httpServletResponse, AuthStatus.MALFORMED_TOKEN, e);
        } catch (JwtException e) {
            sendError(httpServletResponse, AuthStatus.INVALID_TOKEN, e);
        } catch (Exception e) {
            sendError(httpServletResponse, AuthStatus.UNAUTHORIZED, e);
        }
    }

    // json 값으로 오류 반환하기 위한 메소드
    private void sendError(HttpServletResponse response, AuthStatus status, Exception e) throws IOException {
        e.printStackTrace(); // 서버 로그에 예외 전체 스택 출력
        response.setStatus(status.getHttpStatusCode());
        response.setContentType("application/json");
        response.getWriter().write(String.format(
            "{\"code\":%d,\"message\":\"%s\"}",
            status.getCustomStatusCode(),
            status.getDescription()
        ));
    }
}