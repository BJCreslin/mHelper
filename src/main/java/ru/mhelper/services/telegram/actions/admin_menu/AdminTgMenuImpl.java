package ru.mhelper.services.telegram.actions.admin_menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.mhelper.services.geting_code.TelegramCodeService;
import ru.mhelper.services.telegram.actions.answer_services.TelegramTextAnswer;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service("menu")
public class AdminTgMenuImpl implements AdminTgMenu {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminTgMenuImpl.class);

    public static final String GET_MENU_LOGGER = "Get menu for user TelegramId {} with text {}";
    public static final String GET_INFO = "Меню управления данными.";
    public static final String GET_CODE = "Code";
    public static final String SHOW_ALL_CODE = "All code";
    private static final String NUMBER_OPERATION = "number";
    public static final String THERE_ARE_NO_CODES = "There are no codes.";
    public static final String GET_NEW_CODE_BUTTON_NAME = "Get new code";
    public static final String SHOW_ALL_CODES_BUTTON_NAME = "Show all codes";
    public static final String SELECT_MENU_ITEM = "Выберите пункт меню";
    public static final String CREATE_MENU_FOR_USER = "Create menu for user TelegramId {}.";

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
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(CREATE_MENU_FOR_USER, chatId);
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = createButton(GET_NEW_CODE_BUTTON_NAME, GET_CODE);
        InlineKeyboardButton inlineKeyboardButton2 = createButton(SHOW_ALL_CODES_BUTTON_NAME, SHOW_ALL_CODE);

        List<InlineKeyboardButton> keyboardButtonsRow1 = createKeyboardButtonsRow(inlineKeyboardButton1);
        List<InlineKeyboardButton> keyboardButtonsRow2 = createKeyboardButtonsRow(inlineKeyboardButton2);

        List<List<InlineKeyboardButton>> rowList = createRowList(keyboardButtonsRow1, keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(chatId));
        response.setText(SELECT_MENU_ITEM);
        response.setReplyMarkup(inlineKeyboardMarkup);
        return response;
    }

    private List<InlineKeyboardButton> createKeyboardButtonsRow(InlineKeyboardButton... inlineKeyboardButtons) {
        return Arrays.asList(inlineKeyboardButtons);
    }

    @SafeVarargs
    private List<List<InlineKeyboardButton>> createRowList(List<InlineKeyboardButton>... rows) {
        return Arrays.asList(rows);
    }

    private InlineKeyboardButton createButton(String name, String action) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(name);
        inlineKeyboardButton.setCallbackData(action);
        return inlineKeyboardButton;
    }

    @Transactional
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
