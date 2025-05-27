package com.matchday.matchdayserver.common.auth;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.io.IOException;

@Slf4j
@Component
public class JwtTokenFilter extends GenericFilter{

    private final SecretKey secretKey;//SecretKey가 Key보다 하위 인터페이스
    private static final String BEARER_PREFIX = "Bearer ";
    public static final String USERDETAILS_ATTRIBUTE_NAME ="userInfo";
    public  final TokenHelper tokenHelper;

    public JwtTokenFilter(@Value("${jwt.secret}") String secretKey,TokenHelper tokenHelper) {
        this.secretKey = generateSecretKey(secretKey);
        this.tokenHelper = tokenHelper;
    }

    private SecretKey generateSecretKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;//HTTP 전용 메소드를 사용하기 위해 변환
        //토큰 형식 검증(Bearer)
        String jwtToken =resolveToken(httpServletRequest);
        if(StringUtils.hasText(jwtToken) && tokenHelper.validateToken(jwtToken)){
            //문제가 없으면 SecurityContextHolder 에 jwtToken 정보가 담긴 authentication 적재
            Authentication authentication = tokenHelper.getAuthentication(jwtToken);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();//UserDetails를 구현한 사용자 객체를 Return
            request.setAttribute(USERDETAILS_ATTRIBUTE_NAME,userDetails);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request,response);//SecurityFilterChain으로 다시 돌아가기 위한 코드
    }

    //request헤더에서 토큰값을 떼어와, Bearer 값을 검증하고 제거
    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");//Authorization 헤더에서 토큰값을 꺼낸다

        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}
