package com.example.springsecurityjwtpractice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JWTUtil {

    private static final String SEC_KEY = "jwtSecretKey";
    private static final Algorithm ALGORITHM = Algorithm.HMAC512(SEC_KEY);
    private static final String AUTHORITIES_KEY = "auth";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 3;            // 1시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 6;  // 7일

    public static String generateAccessToken(Authentication authentication) {

        List<String> authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        String access_token = JWT.create()
                .withSubject(authentication.getName())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .withClaim(AUTHORITIES_KEY, authorities)
                .sign(ALGORITHM);

        return access_token;
    }

    public static String generateRefreshToken(Authentication authentication) {

        String refresh_token = JWT.create()
                .withSubject(authentication.getName())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .sign(ALGORITHM);

        return refresh_token;
    }

    public static VerifyResult verifyToken(String token) {
        try {
            log.info("Verified token...");
            DecodedJWT verified = JWT.require(ALGORITHM).build().verify(token);
            return VerifyResult.builder()
                    .success(true)
                    .username(verified.getSubject())
                    .build();

        } catch (JWTVerificationException e) {
            log.error("Verifying token failed : {}", e.getMessage());
            DecodedJWT decodedJWT = JWT.decode(token);
            return VerifyResult.builder()
                    .success(false)
                    .username(decodedJWT.getSubject())
                    .build();
        }
    }
}
