package com.serkan.lottofun.authservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorMessage {
    USER_NOT_FOUND("User not found!", HttpStatus.NOT_FOUND),
    USERNAME_NOT_FOUND("Username not found!", HttpStatus.NOT_FOUND),
    USERNAME_ALREADY_EXIST("Username already exist!", HttpStatus.CONFLICT),
    TOKEN_EXPIRED("Token has expired!", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("Invalid token!", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS("Invalid username or password!", HttpStatus.UNAUTHORIZED),
    INSUFFICIENT_BALANCE("Insufficient balance!", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
