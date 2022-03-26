package ru.mhelper.services.telegram.actions.text;

public interface TelegramTextAction {

    String action(Long chatId, String text);

    String getInfo();

}
