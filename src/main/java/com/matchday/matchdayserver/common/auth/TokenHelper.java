package com.matchday.matchdayserver.common.auth;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.JwtStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TokenHelper {

    private final SecretKey secretKey;//SecretKey가 Key보다 하위 인터페이스

    public TokenHelper(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = generateSecretKey(secretKey);
    }

    private SecretKey generateSecretKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //유효한 Jwt 토큰인지 검증
    public boolean validateToken(String token) {
        try{
            Claims claims= getClaims(token);
            return true;
        } catch (SignatureException exception) {
            log.error(exception.getMessage() + "\n" + exception.getStackTrace()
                .toString());
            throw new ApiException(JwtStatus.SIGNATURE_NOT_MATCH);
        } catch (ExpiredJwtException exception) {
            log.error(exception.getMessage() + "\n" + exception.getStackTrace()
                .toString());
            throw new ApiException(JwtStatus.EXPIRED_TOKEN);
        } catch (Exception exception) {
            log.error(exception.getMessage() + "\n" + exception.getStackTrace()
                .toString());
            throw new ApiException(JwtStatus.INVALID_TOKEN);
        }
    }

    public Authentication getAuthentication(String token) {

        //권한(authorities) 추출
        Claims claims=getClaims(token);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+claims.get("role")));
        //세부정보(principal) 추출

        Long userId = Long.valueOf(claims.get("userId").toString());

        //UsernamePasswordAuthenticationToken 객체 생성
        CustomUserDetails userDetails=new CustomUserDetails(userId,claims.getSubject(),authorities); //matchday.user가 아니고 시큐리티user임
        return new UsernamePasswordAuthenticationToken(userDetails,token,userDetails.getAuthorities());
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)//전달받은 토큰이 우리 서버에서 만든건지 검증
            .build()
            .parseSignedClaims(token)//검증할 토큰 전달
            .getPayload();
    }
}
