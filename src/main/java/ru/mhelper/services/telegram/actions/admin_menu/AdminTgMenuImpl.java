package ru.mhelper.services.telegram.actions.admin_menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.mhelper.models.objects.TelegramNameFunctionPair;
import ru.mhelper.services.geting_code.TelegramCodeService;
import ru.mhelper.services.telegram.actions.answer_services.TelegramTextAnswer;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    public static final String GET_ALL_CODES_BUTTON_NAME = "Show all codes";

    public static final String SELECT_MENU_ITEM = "Выберите пункт меню";

    public static final String CREATE_MENU_FOR_USER = "Create menu for user TelegramId {}.";

    private final TelegramCodeService telegramCodeService;

    private final Map<String, TelegramTextAnswer> textActions;

    private final Map<String, TelegramNameFunctionPair> adminMenuItemRow1;

    private final Map<String, TelegramNameFunctionPair> adminMenuItemRow2;

    private final InlineKeyboardMarkup inlineKeyboardMarkup;

    public AdminTgMenuImpl(TelegramCodeService telegramCodeService, Map<String, TelegramTextAnswer> textActions) {
        this.telegramCodeService = telegramCodeService;
        this.textActions = textActions;

        adminMenuItemRow1 = new ConcurrentHashMap<>();
        TelegramNameFunctionPair pair1 = new TelegramNameFunctionPair(GET_NEW_CODE_BUTTON_NAME, this::getNewCodeButtonFunction);
        adminMenuItemRow1.put(GET_CODE, pair1);
        TelegramNameFunctionPair pair2 = new TelegramNameFunctionPair(GET_ALL_CODES_BUTTON_NAME, this::getAllCodeButtonFunction);
        adminMenuItemRow1.put(SHOW_ALL_CODE, pair2);
        adminMenuItemRow2 = new ConcurrentHashMap<>();
        this.inlineKeyboardMarkup = creatInlineKeyboardMarkup();
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
        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(chatId));
        response.setText(SELECT_MENU_ITEM);
        response.setReplyMarkup(inlineKeyboardMarkup);
        return response;
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
        String result = "";
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(GET_MENU_LOGGER, chatId, text);
        }
        if (adminMenuItemRow1.containsKey(text)) {
            result = adminMenuItemRow1.get(text).getAction().apply(chatId);
        }

        if (adminMenuItemRow2.containsKey(text)) {
            result = adminMenuItemRow2.get(text).getAction().apply(chatId);
        }
        return result;
    }

    private String getAllCodeButtonFunction(Long chatId) {
        String result;
        var codes = telegramCodeService.getAllCodes();
        if (codes.isEmpty()) {
            result = THERE_ARE_NO_CODES;
        } else {
            StringBuilder textBuilder = new StringBuilder();
            telegramCodeService.getAllCodes().forEach((k, v) -> textBuilder.append(k).append(" ").append(v.getUserId()).append(" ").append(v.getTimeCreated()).append("\n"));
            result = textBuilder.toString();
        }
        return result;
    }

    private String getNewCodeButtonFunction(Long chatId) {
        return textActions.get(NUMBER_OPERATION).action(chatId, "");
    }

    private InlineKeyboardMarkup creatInlineKeyboardMarkup() {
        var keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        if (!adminMenuItemRow1.isEmpty()) {
            keyboardButtonsRow1 = adminMenuItemRow1.entrySet().stream().map(x -> createButton(x.getValue().getName(), x.getKey())).collect(Collectors.toList());
        }
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        if (!adminMenuItemRow2.isEmpty()) {
            keyboardButtonsRow2 = adminMenuItemRow2.entrySet().stream().map(x -> createButton(x.getValue().getName(), x.getKey())).collect(Collectors.toList());
        }

        List<List<InlineKeyboardButton>> rowList = createRowList(keyboardButtonsRow1, keyboardButtonsRow2);
        keyboardMarkup.setKeyboard(rowList);
        return keyboardMarkup;
    }
}
