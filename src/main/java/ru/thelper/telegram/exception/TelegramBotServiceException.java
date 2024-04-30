package ru.thelper.telegram.exception;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotServiceException extends RuntimeException {

    public static final String ERROR_SEND_MESSAGE = "Error send message to user TelegramId %s with text %s";

    public static final String USER_MUST_HAVE_TELEGRAM_ID = "User with id %d must have Telegram Id.";

    public TelegramBotServiceException(String eMessage, TelegramApiException ex) {
        super(eMessage, ex);
    }

    public TelegramBotServiceException(String eMessage) {
        super(eMessage);
    }
}
