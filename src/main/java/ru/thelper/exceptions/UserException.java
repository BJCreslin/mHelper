package ru.thelper.exceptions;

public class UserException extends RuntimeException {

    public final static String USER_NULL = "Error User is null!";

    public UserException(String message) {
        super(message);
    }
}
