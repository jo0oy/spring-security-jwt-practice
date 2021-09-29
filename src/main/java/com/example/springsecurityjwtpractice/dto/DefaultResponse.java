package com.example.springsecurityjwtpractice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DefaultResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public DefaultResponse(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = null;
    }

    public static<T> DefaultResponse<T> res(final StatusCode status, final ResponseMessage message) {
        return res(status, message, null);
    }

    public static<T> DefaultResponse<T> res(final StatusCode status, final ResponseMessage message, final T t) {
        return DefaultResponse.<T>builder()
                .data(t)
                .statusCode(status.getStatusCode())
                .message(message.getMessage())
                .build();
    }
}
