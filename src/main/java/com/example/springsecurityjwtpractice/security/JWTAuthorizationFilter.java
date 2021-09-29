package com.example.springsecurityjwtpractice.security;

import com.example.springsecurityjwtpractice.application.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {

        String bearerHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (bearerHeader == null || !bearerHeader.startsWith(BEARER_PREFIX)) {
            log.info("no bearer token exists....");
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(bearerHeader);
        VerifyResult verifyResult = JWTUtil.verifyToken(token);

        if (verifyResult.isSuccess()) {
            UserDetails findUser = memberService.loadUserByUsername(verifyResult.getUsername());

            UserDetails principal = new User(findUser.getUsername(), " ", findUser.getAuthorities());

            log.info("make principal without credentials : {}", findUser.getUsername());
            log.info("userdetails authorities : {}", findUser.getAuthorities());
            Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);
        }else{
            log.info("Token is not valid!");
            throw new RuntimeException("Token is not valid");
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(String bearerHeader) {
        return bearerHeader.substring(BEARER_PREFIX.length());
    }
}
