package ru.mhelper.telegram.actions.answer_services;

public interface TelegramTextAnswer {

    String action(Long chatId, String text);

    String getInfo();

}
