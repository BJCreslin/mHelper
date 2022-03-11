package ru.mhelper.services.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.mhelper.services.geting_code.ErrorGettingCode;
import ru.mhelper.services.geting_code.TelegramCodeService;

import java.util.ArrayList;
import java.util.List;

import static ru.mhelper.services.telegram.TelegramBotServiceException.ERROR_SEND_MESSAGE;

@Service
public class Bot extends TelegramLongPollingBot {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    private static final String NUMBER_MESSAGE = "Полученный код: ";
    private static final String NUMBER_ERROR = "Ошибка получения кода. Попробуйте повторить позже.";

    private static final String NUMBER_OPERATION = "number";
    private static final String START_OPERATION = "/start";

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final TelegramCodeService telegramCodeService;

    public Bot(TelegramCodeService telegramCodeService) {
        this.telegramCodeService = telegramCodeService;
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
            Message message = update.getMessage();
            SendMessage response = new SendMessage();
            Long chatId = message.getChatId();
            response.setChatId(String.valueOf(chatId));
            String text = message.getText();
            if (text.equals(NUMBER_OPERATION) || text.equals(START_OPERATION)) {
                try {
                    text = NUMBER_MESSAGE + telegramCodeService.createCode(chatId);
                } catch (ErrorGettingCode e) {
                    LOGGER.error(ErrorGettingCode.TOO_MANY_ATTEMPTS);
                    text = NUMBER_ERROR;
                }
            } else if (text.equals("menu")) {
                response = sendInlineKeyBoardMessage(chatId);
            }
            response.setText(text);
            try {
                execute(response);
            } catch (TelegramApiException ex) {
                String eMessage = String.format(ERROR_SEND_MESSAGE, chatId, text);
                LOGGER.error(eMessage);
                throw new TelegramBotServiceException(eMessage, ex);
            }
        } else if (update.hasCallbackQuery()) {
            try {
                SendMessage response = new SendMessage();
                Long chatId = update.getCallbackQuery().getMessage().getChatId();
                response.setChatId(String.valueOf(chatId));
                response.setText(update.getCallbackQuery().getData());
                response.setChatId(String.valueOf(chatId));
                execute(response);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    public static SendMessage sendInlineKeyBoardMessage(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Алтайский край");
        inlineKeyboardButton1.setCallbackData("Алтайский край");
        inlineKeyboardButton2.setText("Амурская область");
        inlineKeyboardButton2.setCallbackData("Амурская область");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow2.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(chatId));
        response.setText("Выберите область");
        response.setReplyMarkup(inlineKeyboardMarkup);

        return response;
    }
}
