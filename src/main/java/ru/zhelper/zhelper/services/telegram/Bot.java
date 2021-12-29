package ru.zhelper.zhelper.services.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class Bot extends TelegramLongPollingBot {
    private final Logger logger = LoggerFactory.getLogger(Bot.class);

    @Value("telegram_bot_name")
    private static String BOT_NAME;

    @Value("telegram_bot_token")
    private static String BOT_TOKEN;

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            sendMessage.
        }
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Error telegram: {}", e.getMessage());
        }
    }
