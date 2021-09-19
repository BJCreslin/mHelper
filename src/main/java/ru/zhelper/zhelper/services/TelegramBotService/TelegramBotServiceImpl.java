package ru.zhelper.zhelper.services.TelegramBotService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.models.ProcurementType;
import ru.zhelper.zhelper.models.dto.ProcurementAddress;
import ru.zhelper.zhelper.repository.ProcurementRepo;
import ru.zhelper.zhelper.services.ProcurementService;
import ru.zhelper.zhelper.services.exceptions.TelegramBotException;
import ru.zhelper.zhelper.services.validator.URLValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static ru.zhelper.zhelper.services.TelegramBotService.TelegramCommandsEnum.*;

@Service
public class TelegramBotServiceImpl extends TelegramLongPollingBot implements TelegramBotService {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotServiceImpl.class);
    private static final String TEMP_ACTIVATE_KEY = "a12345a";
    private static final String SAY_HELP = "Я ваш помощник в закупках, всегда рад напомнить о ближайщих " +
            "закрывающихся позициях!";
    private static final String SAY_UNKNOWN_COMMAND = "Неизвестная команда или ошибка в ссылке на закупку! Введите "
            + HELP + " для получения справки.";
    private static final String SAY_SUCCESSFUL_ACTIVATION = "Успешная активация!";
    private static final String SAY_ERROR_ACTIVATION = "Ошибка ввода ключа активации, введите ещё раз!";
    private static final String SAY_ENTER_ACTIVATION_KEY = "Введите ключ активации: " + KEY + " <ключ>";
    private static final String SPLIT_SYMBOL_FOR_COMMAND = "\\s";
    private static final String START_SYMBOL_FOR_COMMAND = "/";
    private static final String STRING_FORMAT_FOR_PROCUREMENTS = "id:%d uin:%s date:%s %n";
    private static final String SAY_NO_PROCUREMENTS = "Нет закупок!";
    private static final String PROCESSING_IS_NOT_WORKING_YET = "Обработка этого типа закупок пока не работает!";
    private static final String SENT_FOR_PROCESSING = "Закупка отправлена в обработку";
    private static final String ERROR_IN_SEND_MESSAGE_TO_ALL = "Error in sendMessageToAll method!";
    private static final String ERROR_IN_SEND_MESSAGE_TO_CHAT_ID = "Error in sendMessageToChatId method!";
    private static final String TELEGRAM_BOT_SERVICE = "TelegramBotService, message from userid = {}";

    private final HashSet<Long> verifiedChatId = new HashSet<>();

    private final String username;
    private final String token;
    private final ProcurementRepo procurementRepo;
    private final URLValidator urlValidator;
    private final ProcurementService procurementService;

    public TelegramBotServiceImpl(@Value("${telegram.bot.username}") String username,
                                  @Value("${telegram.bot.token}") String token,
                                  ProcurementRepo procurementRepo,
                                  URLValidator urlValidator,
                                  ProcurementService procurementService) {
        this.username = username;
        this.token = token;
        this.procurementRepo = procurementRepo;
        this.urlValidator = urlValidator;
        this.procurementService = procurementService;
    }

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
        if (logger.isDebugEnabled()) {
            logger.debug(TELEGRAM_BOT_SERVICE, update.getMessage().getChatId());
        }
        Message message = update.getMessage();
        if (message == null || !message.hasText()) {
            return;
        }
        if (message.getText().startsWith(START_SYMBOL_FOR_COMMAND) && commandProcessing(message))
            return;
        if (textProcessing(message)) return;
        sendMsg(message, PROCESSING_IS_NOT_WORKING_YET);
    }

    private boolean commandProcessing(final Message message) {
        String text = message.getText().trim();
        if (text.startsWith(KEY.getTitle())) {
            checkTempActivateKey(message);
            return true;
        }
        if (!isVerifiedChatId(message.getChatId())) {
            sendMsg(message, SAY_ENTER_ACTIVATION_KEY);
            return true;
        }
        if (text.equals(HELP.getTitle())) {
            sendMsg(message, SAY_HELP);
            return true;
        }
        if (text.equals(ALL.getTitle())) {
            sendMsg(message, getAllProcurementsToString());
            return true;
        }
        return false;
    }

    private boolean textProcessing(final Message message) {
        String text = message.getText().trim();
        ProcurementType procurementType = urlValidator.getProcurementType(text);
        if (procurementType == null) {
            sendMsg(message, SAY_UNKNOWN_COMMAND);
            return false;
        }
        if (procurementType == ProcurementType.LAW_615) {
            procurementService.action(ProcurementAddress.builder().address(text).build());
            sendMsg(message, SENT_FOR_PROCESSING);
            return true;
        }
        return false;
    }

    private String getAllProcurementsToString() {
        StringBuilder result = new StringBuilder();
        List<Procurement> procurements = procurementRepo.findAll();
        if (!procurements.isEmpty()) {
            procurements.forEach(p -> result.append(String.format(
                    STRING_FORMAT_FOR_PROCUREMENTS,
                    p.getId(), p.getUin(), p.getApplicationDeadline().toString())
            ));
        } else {
            result.append(SAY_NO_PROCUREMENTS);
        }
        return result.toString();
    }

    private void checkTempActivateKey(final Message message) {
        String text = message.getText().trim();
        if (text.startsWith(KEY.getTitle()) && text.length() < 20) {
            String[] words = text.split(SPLIT_SYMBOL_FOR_COMMAND);
            if (words.length > 1 && words[1].equals(TEMP_ACTIVATE_KEY)) {
                verifiedChatId.add(message.getChatId());
                sendMsg(message, SAY_SUCCESSFUL_ACTIVATION);
            } else {
                sendMsg(message, SAY_ERROR_ACTIVATION);
            }
        }
    }

    private boolean isVerifiedChatId(final long chatId) {
        return verifiedChatId.contains(chatId);
    }

    public void sendMessageToAll(String text) {
        if (!StringUtils.hasText(text)) {
            logger.error(ERROR_IN_SEND_MESSAGE_TO_ALL);
            throw new TelegramBotException(ERROR_IN_SEND_MESSAGE_TO_ALL);
        }
        for (Long chatId : verifiedChatId) {
            if (chatId == null) continue;
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text(text)
                    .build();
            sendMessage.enableMarkdown(true);
            try {
                setDefaultButtons(sendMessage);
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean sendMessageToChatId(String text, long chatId) {
        if (!StringUtils.hasText(text) || chatId < 1) {
            logger.error(ERROR_IN_SEND_MESSAGE_TO_CHAT_ID);
            throw new TelegramBotException(ERROR_IN_SEND_MESSAGE_TO_CHAT_ID);
        }
        if (!isVerifiedChatId(chatId)) return false;

        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(text)
                .build();
        sendMessage.enableMarkdown(true);
        try {
            setDefaultButtons(sendMessage);
            execute(sendMessage);
            return true;
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void sendMsg(final Message message, final String text) {
        if (message == null || !StringUtils.hasText(text)) {
            return;
        }
        SendMessage sendMessage = SendMessage.builder()
                .replyToMessageId(message.getMessageId())
                .chatId(message.getChatId().toString())
                .text(text)
                .build();
        sendMessage.enableMarkdown(true);
        try {
            setDefaultButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void setDefaultButtons(final SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = ReplyKeyboardMarkup.builder()
                .selective(true)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton(HELP.getTitle()));
        keyboardFirstRow.add(new KeyboardButton(ALL.getTitle()));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

}
