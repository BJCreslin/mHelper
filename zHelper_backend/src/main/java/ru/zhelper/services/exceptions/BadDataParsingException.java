package ru.zhelper.services.exceptions;

public class BadDataParsingException extends RuntimeException {
    public BadDataParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
