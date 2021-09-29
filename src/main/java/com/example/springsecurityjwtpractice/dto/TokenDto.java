package com.example.springsecurityjwtpractice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenDto {

    @NoArgsConstructor
    @Getter
    public static class LoginRequest {
        private String username;
        private String password;

        @Builder
        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class RefreshTokeRequest {
        private String refreshToken;

        @Builder
        public RefreshTokeRequest(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Response {
        private String accessToken;
        private String refreshToken;

        @Builder
        public Response(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}
