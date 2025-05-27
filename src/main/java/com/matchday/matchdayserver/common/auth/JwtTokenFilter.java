package com.matchday.matchdayserver.common.auth;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.AuthStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenFilter extends GenericFilter{

    private final SecretKey secretKey;//SecretKey가 Key보다 하위 인터페이스
    private static final String BEARER_PREFIX = "Bearer ";
    public static final String USERINFO_ATTRIBUTE_NAME="userInfo";

    public JwtTokenFilter(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = generateSecretKey(secretKey);
    }

    private SecretKey generateSecretKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;//HTTP 전용 메소드를 사용하기 위해 변환
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String token = httpServletRequest.getHeader("Authorization");//Authorization 헤더에서 토큰값을 꺼낸다
        try{
            if(token !=null){
                if(!token.substring(0,7).equals(BEARER_PREFIX)){//앞에 Bearer공백 붙었는지 확인
                    throw new ApiException(AuthStatus.INVALID_AUTHORIZATION_HEADER);
                }
                String jwtToken = token.substring(7);//Bearer 뜯어내기
                //token 검증 및 claims(playoad) 추출
                Claims claims= Jwts.parser()
                    .verifyWith(secretKey)//전달받은 토큰이 우리 서버에서 만든건지 검증
                    .build()
                    .parseSignedClaims(jwtToken)//검증할 토큰 전달
                    .getPayload();//검증하고 페이로드 꺼내기(PayLoad 내부 key: claim)
                //Authentication객체 생성
                //authorities(권한) 만들기
                List<GrantedAuthority> authorities = new ArrayList<>();//권한이 여러개일 수 있어서 List타입으로
                authorities.add(new SimpleGrantedAuthority("ROLE_"+claims.get("role")));//ROLE_ 붙이는것이 관례// todo 롤값 없으면 안생김
                Long userId = Long.valueOf(claims.get("userId").toString());
                CustomUserDetails userDetails=new CustomUserDetails(userId,claims.getSubject(),authorities); //matchday.user가 아니고 시큐리티user임
                Authentication authentication=new UsernamePasswordAuthenticationToken(userDetails,jwtToken,userDetails.getAuthorities());

                request.setAttribute(USERINFO_ATTRIBUTE_NAME,authentication.getPrincipal());//request에 userDetails 내용 추가
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request,response);//SecurityFilterChain으로 다시 돌아가기 위한 코드
        }catch (Exception e){//예외 발생 시 filterchain이후의 코드 실행시키지 않고 바로 오류 출력
            e.printStackTrace();
            httpServletResponse.setStatus(AuthStatus.UNAUTHORIZED.getHttpStatusCode());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("invalid token");//json 값으로 오류 반환
        }
    }
}
