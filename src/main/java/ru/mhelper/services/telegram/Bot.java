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
import ru.mhelper.services.geting_code.TelegramCodeService;
import ru.mhelper.services.telegram.actions.answer_services.TelegramTextAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static ru.mhelper.services.telegram.TelegramBotServiceException.ERROR_SEND_MESSAGE;

@Service
public class Bot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    private static final String NUMBER_OPERATION = "number";

    private static final String START_OPERATION = "/start";

    public static final String GET_CODE = "Code";

    public static final String SHOW_ALL_CODE = "All code";

    public static final String THERE_ARE_NO_CODES = "There are no codes.";

    @Value("${bot.name}")
    private String botName;

    @Value("${bot.token}")
    private String botToken;

    private final TelegramCodeService telegramCodeService;

    private final Map<String, TelegramTextAnswer> textActions;

    public Bot(TelegramCodeService telegramCodeService, Map<String, TelegramTextAnswer> textActions) {
        this.telegramCodeService = telegramCodeService;
        this.textActions = textActions;
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
            if (textActions.containsKey(text.toLowerCase(Locale.ROOT))) {
                text = textActions.get(text.toLowerCase(Locale.ROOT)).action(chatId, text);
            } else if (text.equals("menu")) {
                response = sendInlineKeyBoardMessage(chatId);
            } else {
                textActions.get("help").action(chatId, text);
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
                String text = update.getCallbackQuery().getData();
                text = getMenuMessageText(chatId, text);
                response.setText(text);
                execute(response);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    private String getMenuMessageText(Long chatId, String text) {
        if (text.equals(GET_CODE)) {
            text = textActions.get(NUMBER_OPERATION).action(chatId, text);
        }
        if (text.equals(SHOW_ALL_CODE)) {
            var codes = telegramCodeService.getAllCodes();
            if (codes.isEmpty()) {
                text = THERE_ARE_NO_CODES;
            } else {
                StringBuilder textBuilder = new StringBuilder();
                telegramCodeService.getAllCodes().forEach((k, v) -> textBuilder.append(k).append(" ").append(v.getUserId()).append(" ").append(v.getTimeCreated()).append("\n"));
                text = textBuilder.toString();
            }
        }
        return text;
    }

    public static SendMessage sendInlineKeyBoardMessage(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Get new code");
        inlineKeyboardButton1.setCallbackData(GET_CODE);
        inlineKeyboardButton2.setText("Show all codes");
        inlineKeyboardButton2.setCallbackData(SHOW_ALL_CODE);
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
        response.setText("Выберите пункт меню");
        response.setReplyMarkup(inlineKeyboardMarkup);
        return response;
    }
}
