package com.serkan.lottofun.drawservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorMessage {
    DRAW_NOT_FOUND("Draw not found!", HttpStatus.NOT_FOUND),
    DRAW_NOT_OPEN("Ticket sales are closed! Draw is not open.", HttpStatus.BAD_REQUEST),
    DRAW_EXPIRED("Draw time has expired!", HttpStatus.BAD_REQUEST);


    private final String message;
    private final HttpStatus httpStatus;

    ErrorMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
