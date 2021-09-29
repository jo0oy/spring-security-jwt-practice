package com.example.springsecurityjwtpractice.dto;

import lombok.Getter;

@Getter
public enum StatusCode {
    OK(200, "OK"),
    CREATED(201, "CREATED"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    UNAUTHORIZED(401, "UNAUTHORIZED"),
    FORBIDDEN(403, "FORBIDDEN"),
    NOT_FOUND(404, "NOT_FOUND"),
    INTERNAL_SERER_ERROR(500, "INTERNAL_SERVER_ERROR");

    int statusCode;
    String codeMessage;

    StatusCode(int statusCode, String codeMessage) {
        this.statusCode = statusCode;
        this.codeMessage = codeMessage;
    }
}
