package ru.mhelper.exceptions;

public class DaoException extends RuntimeException {
    public static final String ERROR_GET_ROLE = "ERROR_GET_ROLE";

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(String message) {
        super(message);
    }
}
