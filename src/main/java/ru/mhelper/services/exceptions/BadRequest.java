package ru.mhelper.services.exceptions;

public class BadRequest extends RuntimeException {
    public static final String CODE_NOT_FOUND = "Telegram Code not found ";

    public BadRequest(String message, Exception exception) {
        super(message, exception);
    }

    public BadRequest(String message) {
        super(message);
    }
}
