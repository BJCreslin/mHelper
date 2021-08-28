package ru.zhelper.zhelper.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class TelegramBotService extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String username;
    @Value("${telegram.bot.token}")
    private String token;
    private static final String TEMP_ACTIVATE_KEY = "a12345a";
    private final HashSet<Long> verifiedUsers = new HashSet<>();

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
            String text = message.getText();
            if (text.startsWith("/key ") && text.length() < 20) {
                String [] words = text.split(" ");
                if (words.length > 1 && words[1].equals(TEMP_ACTIVATE_KEY)) {
                    verifiedUsers.add(message.getFrom().getId());
                    sendMsg(message, "Успешная активация!");
                } else {
                    sendMsg(message, "Ошибка ввода ключа активации");
                }
            }
            if (!checkingUsers(message.getFrom().getId())) {
                sendMsg(message, "введите ключ активации /key <ключ>");
                return;
            }
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

    private boolean checkingUsers(Long userId) {
        return verifiedUsers.contains(userId);
    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }



    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}
