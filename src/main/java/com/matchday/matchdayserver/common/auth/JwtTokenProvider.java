package com.matchday.matchdayserver.common.auth;

import com.matchday.matchdayserver.auth.model.dto.enums.JwtTokenType;
import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.JwtStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.flywaydb.core.internal.parser.TokenType;
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
    private static final long ACCESS_TOKEN_EXPIRE_TIME_IN_MILLISECONDS = 1000L * 60 * 10 ; // 10min
    private static final long REFRESH_TOKEN_EXPIRE_TIME_IN_MILLISECONDS = 1000L * 60 * 60 * 24; // 1d

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = generateSecretKey(secretKey);
    }

    //토큰 만들기
    //todo:payload에 롤값 넣기
    public String createToken(String subject, Map<String, ?> payload, JwtTokenType type) {
        //jwt토큰의 payload는 여러개의 claims 집합
        Claims claims=null;
        if(type==JwtTokenType.ACCESS){
            claims= generateClaims(subject, payload,ACCESS_TOKEN_EXPIRE_TIME_IN_MILLISECONDS);

        } else if (type==JwtTokenType.REFRESH){
            claims=generateClaims(subject, payload,REFRESH_TOKEN_EXPIRE_TIME_IN_MILLISECONDS);
        } else {
            throw new ApiException(JwtStatus.INVALID_TOKEN_TYPE);
        }

        String token=Jwts.builder() //시그니처 부분 만들기
            .claims(claims)
            .claim("tokenType", type)
            .signWith(secretKey,Jwts.SIG.HS256)
            .compact();
        return token;
    }

    private Claims generateClaims(String subject, Map<String, ?> payload, Long expiration) {
        Date now = new Date();//발행시간
        Date expirationAt = new Date(now.getTime()+expiration);//gettime은 밀리초 단위이므로 분으로 변경하기
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