package ru.zhelper.zhelper.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class TelegramBotService extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String username;
    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help":
                    sendMsg(message, "чем могу помочь?");
                    break;
                case "/setting":
                    sendMsg(message, "что будем настраивать?");
                    break;
                default:
            }
        }
    }

    private void sendMsg(Message message, String text) {
        SendMessage toSendMessage = new SendMessage();
        toSendMessage.enableMarkdown(true);
        toSendMessage.setChatId(message.getChatId().toString());
        toSendMessage.setReplyToMessageId(message.getMessageId());
        toSendMessage.setText(text);
        try {
            execute(toSendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}
