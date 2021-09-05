package ru.zhelper.zhelper.services.TelegramBotService;

public interface TelegramBotService {

    void sendMessageToAll(String text);
    boolean sendMessageToChatId(String text, long chatId);
}
