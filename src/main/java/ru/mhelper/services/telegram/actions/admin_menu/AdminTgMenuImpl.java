package ru.mhelper.services.telegram.actions.admin_menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.mhelper.services.geting_code.TelegramCodeService;
import ru.mhelper.services.telegram.actions.answer_services.TelegramTextAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("menu")
public class AdminTgMenuImpl implements AdminTgMenu {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminTgMenuImpl.class);

    public static final String GET_MENU_LOGGER = "Get menu for user {} with text {}";

    public static final String GET_INFO = "Меню управления данными.";

    public static final String GET_CODE = "Code";

    public static final String SHOW_ALL_CODE = "All code";

    private static final String NUMBER_OPERATION = "number";

    public static final String THERE_ARE_NO_CODES = "There are no codes.";

    private final TelegramCodeService telegramCodeService;

    private final Map<String, TelegramTextAnswer> textActions;

    public AdminTgMenuImpl(TelegramCodeService telegramCodeService, Map<String, TelegramTextAnswer> textActions) {
        this.telegramCodeService = telegramCodeService;
        this.textActions = textActions;
    }

    @Override
    public String getInfo() {
        return GET_INFO;
    }

    @Override
    public SendMessage sendInlineKeyBoardMessage(long chatId) {
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

    public String getMenuMessageText(Long chatId, String text) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(GET_MENU_LOGGER, chatId, text);
        }
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
}
