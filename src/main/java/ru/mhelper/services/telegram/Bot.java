package ru.mhelper.services.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.mhelper.models.users.User;
import ru.mhelper.services.telegram.actions.admin_menu.AdminTgMenu;
import ru.mhelper.services.telegram.actions.answer_services.PrintAllFunctionTextAnswer;
import ru.mhelper.services.telegram.actions.answer_services.TelegramTextAnswer;

import java.util.Locale;
import java.util.Map;

import static ru.mhelper.services.telegram.TelegramBotServiceException.ERROR_SEND_MESSAGE;
import static ru.mhelper.services.telegram.TelegramBotServiceException.USER_MUST_HAVE_TELEGRAM_ID;

@Service
public class Bot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;


    private final Map<String, TelegramTextAnswer> textActions;

    private final AdminTgMenu adminTgMenu;

    public Bot(Map<String, TelegramTextAnswer> textActions, AdminTgMenu adminTgMenu) {
        this.textActions = textActions;
        this.adminTgMenu = adminTgMenu;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            messageEvent(update);
        } else if (update.hasCallbackQuery()) {
            callbackEvent(update);
        }
    }

    public void sendMessageToUser(User user, String text) {
        SendMessage message = new SendMessage();
        if (user.getTelegramUserId() != null) {
            message.setChatId(String.valueOf(user.getTelegramUserId()));
            message.setText(text);
            try {
                execute(message);
            } catch (TelegramApiException ex) {
                String eMessage = String.format(ERROR_SEND_MESSAGE, user.getTelegramUserId(), text);
                LOGGER.error(eMessage);
                throw new TelegramBotServiceException(eMessage, ex);
            }
        } else {
            String eMessage = String.format(USER_MUST_HAVE_TELEGRAM_ID, user.getId());
            LOGGER.error(eMessage);
            throw new TelegramBotServiceException(eMessage);
        }
    }

    private void callbackEvent(Update update) {
        SendMessage response = new SendMessage();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        response.setChatId(String.valueOf(chatId));
        String text = update.getCallbackQuery().getData();
        var sendText = adminTgMenu.getMenuMessageText(chatId, text);
        response.setText(sendText);
        try {
            execute(response);
        } catch (TelegramApiException ex) {
            String eMessage = String.format(ERROR_SEND_MESSAGE, chatId, sendText);
            LOGGER.error(eMessage);
            throw new TelegramBotServiceException(eMessage, ex);
        }
    }

    private void messageEvent(Update update) {
        Message message = update.getMessage();
        SendMessage response = new SendMessage();
        Long chatId = message.getChatId();
        response.setChatId(String.valueOf(chatId));
        String text = message.getText();
        if (textActions.containsKey(text.toLowerCase(Locale.ROOT))) {
            text = textActions.get(text.toLowerCase(Locale.ROOT)).action(chatId, text);
        } else if (text.equalsIgnoreCase("menu")) {
            response = adminTgMenu.sendInlineKeyBoardMessage(chatId);
        } else {
            textActions.get(PrintAllFunctionTextAnswer.SERVICE_NAME).action(chatId, text);
        }
        response.setText(text);
        try {
            execute(response);
        } catch (TelegramApiException ex) {
            String eMessage = String.format(ERROR_SEND_MESSAGE, chatId, text);
            LOGGER.error(eMessage);
            throw new TelegramBotServiceException(eMessage, ex);
        }
    }
}

