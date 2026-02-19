package com.serkan.lottofun.ticketservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorMessage {
    DRAW_NOT_FOUND("Draw not found!", HttpStatus.NOT_FOUND),
    TICKET_NOT_FOUND("Ticket not found!", HttpStatus.NOT_FOUND),
    DRAW_NOT_OPEN("Ticket sales are closed! Draw is not open.", HttpStatus.BAD_REQUEST),
    DRAW_EXPIRED("Draw time has expired!", HttpStatus.BAD_REQUEST),
    INVALID_NUMBER_COUNT("Exactly 5 numbers must be selected!", HttpStatus.BAD_REQUEST),
    INVALID_NUMBER_RANGE("Numbers must be between 1 and 49!", HttpStatus.BAD_REQUEST),
    DUPLICATE_NUMBERS("Numbers must be unique!", HttpStatus.BAD_REQUEST),
    NOT_EXIST_NUMBER("Numbers cannot be null!", HttpStatus.BAD_REQUEST),
    TICKET_SOLD("Ticket already sold",HttpStatus.CONFLICT),
    USER_NOT_FOUND("User not found!", HttpStatus.NOT_FOUND),
    TOKEN_EXPIRED("Token has expired!", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("Invalid token!", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorMessage(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}