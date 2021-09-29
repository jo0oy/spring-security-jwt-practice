package com.example.springsecurityjwtpractice.security;

import com.example.springsecurityjwtpractice.dto.MemberDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    public static final String BEARER_PREFIX = "Bearer ";

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/v1/login");
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows(IOException.class)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        MemberDto.SaveRequest loginDto = objectMapper.readValue(request.getInputStream(), MemberDto.SaveRequest.class);
        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword(), null);

        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetails member = (UserDetails) authResult.getPrincipal();

        String access_token = JWTUtil.generateAccessToken(authResult);
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + access_token);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access-token", access_token);
//        response.getOutputStream().write(objectMapper.writeValueAsBytes(new MemberSecureResponseDto(member)));
        objectMapper.writeValue(response.getOutputStream(), tokens);
    }
}
