package com.example.springsecurityjwtpractice.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtSimpleTest {

    @Test
    @DisplayName("1. jjwt를 이용한 토큰 테스트")
    void jjwtTokenTest() {
        String jjwt_token = Jwts.builder().addClaims(
                Map.of("name", "jooyeon", "code", 100)
        ).signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();

        printToken(jjwt_token);
    }

    @Test
    @DisplayName("2. java-jwt를 이용한 토큰 테스트")
    void javaJwtTokenTest() {
        String oauth0_token = JWT.create().withClaim("name", "jooyeon")
                .withClaim("code", 100)
                .sign(Algorithm.HMAC512("secretKey"));

        printToken(oauth0_token);
    }

    @Test
    @DisplayName("3. jjwt token decode 테스트")
    void parse_jjwt_test (){
        String jjwt_token = Jwts.builder().addClaims(
                Map.of("name", "jooyeon", "code", 100)
        ).signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();

        Jws<Claims> token_info = Jwts.parser().setSigningKey("secretKey")
                .parseClaimsJws(jjwt_token);

        System.out.println(token_info);
    }

    @Test
    @DisplayName("4. java-jwt decode 테스트")
    void parse_javaJwt_test() {
        final Algorithm alg = Algorithm.HMAC512("secretKey");

        String oauth0_token = JWT.create().withClaim("name", "jooyeon")
                .withClaim("code", 100)
                .sign(alg);

        DecodedJWT verified = JWT.require(alg).build().verify(oauth0_token);
        System.out.println(verified.getClaims());
    }

    @Test
    @DisplayName("5. jjwt 와 java-jwt 토큰을 jjwt 방식으로 parsing 테스트")
    void parse_tokensByJjwt_test() {

        String secretKey = "secretKey";
        byte[] SEC_KEY = DatatypeConverter.parseBase64Binary(secretKey);
        String oauth0_token = JWT.create().withClaim("name", "jooyeon")
                .withClaim("code", 100)
                .sign(Algorithm.HMAC512(SEC_KEY));

        String jjwt_token = Jwts.builder().addClaims(
                Map.of("name", "jooyeon", "code", 100)
        ).signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        Jws<Claims> oauth_tokenInfo = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(oauth0_token);
        Jws<Claims> jjwt_tokenInfo = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jjwt_token);

        System.out.println(oauth_tokenInfo);
        System.out.println(jjwt_tokenInfo);
    }

    @Test
    @DisplayName("6. jjwt 와 java-jwt 토큰을 java-jwt 방식으로 parsing 테스트")
    void parse_tokensByOauth0_test() {

        String secretKey = "secretKey";
        byte[] SEC_KEY = DatatypeConverter.parseBase64Binary(secretKey);
        String oauth0_token = JWT.create().withClaim("name", "jooyeon")
                .withClaim("code", 100)
                .sign(Algorithm.HMAC512(SEC_KEY));

        String jjwt_token = Jwts.builder().addClaims(
                Map.of("name", "jooyeon", "code", 100)
        ).signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        DecodedJWT oauth0_verified = JWT.require(Algorithm.HMAC512(SEC_KEY)).build().verify(oauth0_token);
        DecodedJWT jjwt_verified = JWT.require(Algorithm.HMAC512(SEC_KEY)).build().verify(jjwt_token);

        System.out.println(oauth0_verified.getClaims());
        System.out.println(jjwt_verified.getClaims());
    }

    @Test
    @DisplayName("7. 만료 시간 테스트")
    void token_expire_test() throws InterruptedException {
        Algorithm algorithm = Algorithm.HMAC512("secretKey");
        String token = JWT.create().withSubject("a1234")
                .withClaim("name", "jooyeon")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000))
                .sign(algorithm);

        assertThatThrownBy(() -> {
            Thread.sleep(2000);
            JWT.require(algorithm).build().verify(token);
        }).isInstanceOf(TokenExpiredException.class);
    }

    private void printToken(String token) {
        String[] tokens = token.split("\\.");

        System.out.println("header : " + new String(Base64.getDecoder().decode(tokens[0])));
        System.out.println("payload : " + new String(Base64.getDecoder().decode(tokens[1])));
    }
}
