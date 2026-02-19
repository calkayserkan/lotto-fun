package com.serkan.lottofun.ticketservice.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class BaseException extends RuntimeException{
    private final HttpStatus httpStatus;
    public BaseException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.httpStatus = errorMessage.getHttpStatus();
    }
    public BaseException(ErrorMessage errorMessage, String detailMessage) {
        super(errorMessage.getMessage() + " " + detailMessage);
        this.httpStatus = errorMessage.getHttpStatus();
    }
}