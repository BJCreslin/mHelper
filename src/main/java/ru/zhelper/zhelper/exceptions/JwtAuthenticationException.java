package ru.zhelper.zhelper.exceptions;

public class JwtAuthenticationException extends RuntimeException {
    public static final String JWT_IS_INVALID = "JWT token is expired or invalid";

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
