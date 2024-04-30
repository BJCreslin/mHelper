package ru.thelper.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JwtAuthenticationException extends RuntimeException {
    public static final String JWT_IS_INVALID = "JWT token is expired or invalid";
    private final HttpStatus httpStatus;

    public JwtAuthenticationException(String message) {
        super(message);
        httpStatus = HttpStatus.BAD_REQUEST;
    }

    public JwtAuthenticationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
        httpStatus = HttpStatus.BAD_REQUEST;
    }
}
