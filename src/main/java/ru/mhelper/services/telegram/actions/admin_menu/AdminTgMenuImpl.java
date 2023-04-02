package ru.mhelper.services.telegram.actions.admin_menu;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.mhelper.cfg.Constants;
import ru.mhelper.models.objects.TelegramNameFunctionPair;
import ru.mhelper.repository.ProcurementRepository;
import ru.mhelper.repository.UserRepository;
import ru.mhelper.services.geting_code.TelegramCodeService;
import ru.mhelper.services.telegram.actions.answer_services.TelegramTextAnswer;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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

    public static final String GET_ALL_USERS = "all users";

    public static final String GET_ALL_PROCUREMENTS = "all procurements";

    public static final String GET_USERS_LIST_BUTTON_NAME = "Get users list.";

    public static final String GET_PROCUREMENTS_HEADER_NAME = "Get procurements list.";

    public static final String ALL_USERS_HEADER_NAME = "Все пользователи";

    public static final String GET_PROCUREMENTS_LIST_BUTTON_NAME = "Все закупки";

    private final TelegramCodeService telegramCodeService;

    private final UserRepository userRepository;

    private final ProcurementRepository procurementRepository;

    private final Map<String, TelegramTextAnswer> textActions;

    private final Map<String, TelegramNameFunctionPair> adminMenuItemRow1;

    private final Map<String, TelegramNameFunctionPair> adminMenuItemRow2;

    private final Map<String, TelegramNameFunctionPair> adminMenuItemRow3;

    private final InlineKeyboardMarkup inlineKeyboardMarkup;

    public AdminTgMenuImpl(TelegramCodeService telegramCodeService, UserRepository userRepository, ProcurementRepository procurementRepository, Map<String, TelegramTextAnswer> textActions) {
        this.telegramCodeService = telegramCodeService;
        this.userRepository = userRepository;
        this.procurementRepository = procurementRepository;
        this.textActions = textActions;

        adminMenuItemRow1 = new ConcurrentHashMap<>();
        var pair1 = new TelegramNameFunctionPair(GET_NEW_CODE_BUTTON_NAME, this::getNewCodeButtonFunction);
        adminMenuItemRow1.put(GET_CODE, pair1);
        var pair2 = new TelegramNameFunctionPair(GET_ALL_CODES_BUTTON_NAME, this::getAllCodeButtonFunction);
        adminMenuItemRow1.put(SHOW_ALL_CODE, pair2);

        adminMenuItemRow2 = new ConcurrentHashMap<>();
        var pair3 = new TelegramNameFunctionPair(GET_USERS_LIST_BUTTON_NAME, this::getAllUsersListFunction);
        adminMenuItemRow2.put(GET_ALL_USERS, pair3);

        adminMenuItemRow3 = new ConcurrentHashMap<>();
        var pair4 = new TelegramNameFunctionPair(GET_PROCUREMENTS_LIST_BUTTON_NAME, this::getAllProcurementsListFunction);
        adminMenuItemRow3.put(GET_ALL_PROCUREMENTS, pair4);

        this.inlineKeyboardMarkup = creatInlineKeyboardMarkup();
    }

    private String getAllProcurementsListFunction(Long chatId) {
        StringBuilder result = new StringBuilder(GET_PROCUREMENTS_HEADER_NAME).append(System.lineSeparator());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(GET_PROCUREMENTS_LIST_BUTTON_NAME);
        }
        var procurements = procurementRepository.findAll();
        procurements.forEach(x -> result.append(x.getId()).append("| ").append(x.getUin()).append("| ").append(x.getFzNumber()).append("| ").append(x.getObjectOf(), 0, 20).append(System.lineSeparator()));
        return result.toString();
    }

    private String getAllUsersListFunction(Long chatId) {
        StringBuilder result = new StringBuilder(ALL_USERS_HEADER_NAME).append(System.lineSeparator());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(GET_USERS_LIST_BUTTON_NAME);
        }
        var users = userRepository.findAll();
        users.forEach(x -> result.append(x.getId()).append("| ").append(x.getUsername()).append(System.lineSeparator()));
        return result.toString();
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
        if (adminMenuItemRow3.containsKey(text)) {
            result = adminMenuItemRow3.get(text).getAction().apply(chatId);
        }
        return result;
    }

    private String getAllCodeButtonFunction(Long chatId) {
        String result;
        var codes = telegramCodeService.getAllCodes();
        if (codes.isEmpty()) {
            result = THERE_ARE_NO_CODES;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
            StringBuilder textBuilder = new StringBuilder();
            telegramCodeService.getAllCodes().forEach((k, v) -> textBuilder.append(k).append(" ").append(v.getUserId()).append(" ").append(v.getTimeCreated().format(formatter)).append(System.lineSeparator()));
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
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        if (!adminMenuItemRow3.isEmpty()) {
            keyboardButtonsRow3 = adminMenuItemRow3.entrySet().stream().map(x -> createButton(x.getValue().getName(), x.getKey())).collect(Collectors.toList());
        }

        List<List<InlineKeyboardButton>> rowList = createRowList(keyboardButtonsRow1, keyboardButtonsRow2, keyboardButtonsRow3);
        keyboardMarkup.setKeyboard(rowList);
        return keyboardMarkup;
    }
}
