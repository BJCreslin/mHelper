package ru.mhelper.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JwtAuthenticationException extends RuntimeException {
    public static final String JWT_IS_INVALID = "JWT token is expired or invalid";
    private HttpStatus httpStatus;

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
