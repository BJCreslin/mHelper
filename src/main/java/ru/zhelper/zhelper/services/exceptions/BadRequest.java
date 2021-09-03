package ru.zhelper.zhelper.services.exceptions;

public class BadRequest extends RuntimeException {
    public BadRequest(String message, Exception exception) {
        super(message, exception);
    }

    public BadRequest(String message) {
        super(message);
    }
}
