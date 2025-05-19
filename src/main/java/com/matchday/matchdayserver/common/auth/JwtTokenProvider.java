package com.matchday.matchdayserver.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final int expiration;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,@Value("${jwt.expiration}") final int expiration) {
        this.secretKey = generateSecretKey(secretKey);
        this.expiration = expiration;
    }

    //토큰 만들기
    //todo:payload에 롤값 넣기
    public String createToken(String subject, Map<String, ?> payload) {
        //jwt토큰의 payload는 여러개의 claims 집합
        Claims claims= generateClaims(subject, payload);

        String token=Jwts.builder() //시그니처 부분 만들기
            .claims(claims)
            .signWith(secretKey,Jwts.SIG.HS256)
            .compact();
        return token;
    }

    private Claims generateClaims(String subject, Map<String, ?> payload) {
        Date now = new Date();//발행시간
        Date expirationAt = new Date(now.getTime()+expiration*60*1000L);//gettime은 밀리초 단위이므로 분으로 변경하기
        Claims claims = Jwts.claims()
            .subject(subject)
            .issuedAt(now)
            .expiration(expirationAt)
            .add(payload)
            .build();
        return claims;
    }

    //salt(secret)을 이용해 만들어진 Key 로 Jwt의 signature 만듦
    private SecretKey generateSecretKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}