package ru.mhelper.services.geting_code;

public class ErrorGettingCode extends RuntimeException {

    public static final String TOO_MANY_ATTEMPTS = "Too many attempts to get the code";

    public static final String NO_CODES = "No any codes";

    public static final String NO_CURRENT_CODE = "No current code: %d";

    public ErrorGettingCode(String message) {
        super(message);
    }
}
