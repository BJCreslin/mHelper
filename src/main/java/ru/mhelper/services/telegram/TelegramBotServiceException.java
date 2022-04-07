package ru.mhelper.services.telegram;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBotServiceException extends RuntimeException {

    public static final String ERROR_SEND_MESSAGE = "Error send message to user TelegramId %s with text %s";

    public TelegramBotServiceException(String eMessage, TelegramApiException ex) {
        super(eMessage, ex);
    }
}
