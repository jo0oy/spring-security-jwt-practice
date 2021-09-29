package com.example.springsecurityjwtpractice.dto;

import com.example.springsecurityjwtpractice.domain.Member;
import com.example.springsecurityjwtpractice.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class MemberDto {

    @NoArgsConstructor
    @Getter
    public static class SaveRequest {
        private String username;
        private String password;
        private String email;
        private Role role;

        @Builder
        public SaveRequest(String username, String password, String email, Role role) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.role = role;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class Info {
        private Long userId;
        private String username;
        private String password;
        private String email;
        private String role;

        public Info(Member entity) {
            this.userId = entity.getId();
            this.username = entity.getUsername();
            this.password = entity.getPassword();
            this.email = entity.getEmail();
            this.role = entity.getRole().getValue();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class SimpleInfo {
        private Long userId;
        private String username;
        private String role;

        public SimpleInfo(Member entity) {
            this.userId = entity.getId();
            this.username = entity.getUsername();
            this.role = entity.getRole().getValue();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class ListResponse<T> {

        private int total;
        private List<T> members = new ArrayList<>();

        public ListResponse(List<T> entities) {
            this.total = entities.size();
            members.addAll(entities);
        }

    }

}
