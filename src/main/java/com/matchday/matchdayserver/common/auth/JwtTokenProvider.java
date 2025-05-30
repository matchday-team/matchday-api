package com.matchday.matchdayserver.common.auth;

import com.matchday.matchdayserver.auth.model.dto.enums.JwtTokenType;
import com.matchday.matchdayserver.user.model.entity.User;
import com.matchday.matchdayserver.user.model.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME_IN_MILLISECONDS = 1000L * 60 * 10 ; // 10min
    private static final long REFRESH_TOKEN_EXPIRE_TIME_IN_MILLISECONDS = 1000L * 60 * 60 * 24; // 1d

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = generateSecretKey(secretKey);
    }

    public String createToken(User user, JwtTokenType type) {

        Claims claims = generateClaims(user, type);

        return Jwts.builder() //시그니처 부분 만들기
            .claims(claims)
            .claim("tokenType", type)
            .signWith(secretKey,Jwts.SIG.HS256)
            .compact();
    }

    private Claims generateClaims(User user,JwtTokenType type) {

        Long userId = user.getId();
        String email = user.getEmail();
        Role role = user.getRole();

        String subject=email;

        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("role", role);

        Date now = new Date();//발행시간
        long expiration = switch (type) {
            case ACCESS -> ACCESS_TOKEN_EXPIRE_TIME_IN_MILLISECONDS;
            case REFRESH -> REFRESH_TOKEN_EXPIRE_TIME_IN_MILLISECONDS;
        };
        Date expirationAt = new Date(now.getTime()+expiration);

        return Jwts.claims()
            .subject(subject)
            .issuedAt(now)
            .expiration(expirationAt)
            .add(payload)
            .build();
    }

    //salt(secret)을 이용해 만들어진 Key 로 Jwt의 signature 만듦
    private SecretKey generateSecretKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}