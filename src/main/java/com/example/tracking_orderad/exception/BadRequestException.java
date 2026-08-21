package com.example.tracking_orderad.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BadRequestException extends RuntimeException {
    private final HttpStatus status;
    public BadRequestException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
