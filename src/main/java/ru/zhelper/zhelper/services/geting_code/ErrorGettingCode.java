package ru.zhelper.zhelper.services.geting_code;

public class ErrorGettingCode extends RuntimeException {
    public final static String TOO_MANY_ATTEMPTS = "Too many attempts to get the code";

    public ErrorGettingCode(String message) {
        super(message);
    }
}
