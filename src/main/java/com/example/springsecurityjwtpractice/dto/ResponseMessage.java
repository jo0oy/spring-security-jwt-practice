package com.example.springsecurityjwtpractice.dto;

import lombok.Getter;

@Getter
public enum ResponseMessage {

    LOGIN_SUCCESS("로그인 성공"),
    LOGIN_FAIL("로그인 실패"),
    CREATED_MEMBER("회원가입 성공"),
    CREATE_MEMBER_FAIL("회원가입 실패"),
    UNAUTHORIZED("요청 거부 : 인증 정보 없음"),
    FORBIDDEN("요청 거부 : 권한 없음"),
    READ_MEMBER("단일 회원 조회 성공"),
    READ_MEMBERS("회원 리스트 조회 성공"),
    NOT_FOUND_MEMBER("회원정보를 찾을 수 없음."),
    UPDATED_MEMBER("회원정보 수정 성공"),
    DELETE_MEMBER("회원 탈퇴 성공"),
    RES_SUCCESS("응답 성공");

    private String message;

    ResponseMessage(String message) {
        this.message = message;
    }
}
