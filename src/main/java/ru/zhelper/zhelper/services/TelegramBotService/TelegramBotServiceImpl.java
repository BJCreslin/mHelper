package ru.zhelper.zhelper.services.TelegramBotService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
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
import ru.zhelper.zhelper.services.validator.URLValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class TelegramBotServiceImpl extends TelegramLongPollingBot implements TelegramBotService {

    private static final String TEMP_ACTIVATE_KEY = "a12345a";
    private static final String COMMAND_HELP = "/help";
    private static final String COMMAND_ALL = "/all";
    private static final String COMMAND_KEY = "/key";
    private static final String SAY_HELP = "Я ваш помощник в закупках, всегда рад напомнить о ближайщих " +
            "закрывающихся позициях!";
    private static final String SAY_UNKNOWN_COMMAND = "Неизвестная команда или ошибка в ссылке на закупку! Введите " + COMMAND_HELP +
            " для получения справки.";
    private static final String SAY_SUCCESSFUL_ACTIVATION =  "Успешная активация!";
    private static final String SAY_ERROR_ACTIVATION =  "Ошибка ввода ключа активации, введите ещё раз!";
    private static final String SAY_ENTER_ACTIVATION_KEY = "Введите ключ активации: " + COMMAND_KEY + " <ключ>";
    private static final String SPLIT_SYMBOL_FOR_COMMAND = "\\s";
    private static final String STRING_FORMAT_FOR_PROCUREMENTS = "id:%d uin:%s date:%s %n";
    private static final String SAY_NO_PROCUREMENTS = "Нет закупок!";
    private static final String PROCESSING_IS_NOT_WORKING_YET = "Обработка этого типа закупок пока не работает!";
    private static final String SENT_FOR_PROCESSING = "Закупка отправлена в обработку";

    private final HashSet<Long> verifiedChatId = new HashSet<>();

    private final String username;
    private final String token;
    private final ProcurementRepo procurementRepo;
    private final ProcurementService  procurementService;
    private final URLValidator urlValidator;

    public TelegramBotServiceImpl(@Value("${telegram.bot.username}") String username,
                                  @Value("${telegram.bot.token}") String token,
                                  ProcurementRepo procurementRepo,
                                  @Lazy ProcurementService procurementService,
                                  URLValidator urlValidator) {
        this.username = username;
        this.token = token;
        this.procurementRepo = procurementRepo;
        this.procurementService = procurementService;
        this.urlValidator = urlValidator;
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
        Message message = update.getMessage();
        if (this.commandProcessing(message)) return;
        if (this.textProcessing(message)) return;
        this.sendMsg(message, PROCESSING_IS_NOT_WORKING_YET);
    }

    private boolean commandProcessing(final Message message) {
        if (message == null || !message.hasText()) {
            return false; //todo telegram exception
        }
        String text = message.getText().trim();
        if (text.startsWith(COMMAND_KEY)) {
            this.checkTempActivateKey(message);
            return true;
        }
        if (!this.isVerifiedChatId(message.getChatId())) {
            this.sendMsg(message, SAY_ENTER_ACTIVATION_KEY);
            return true;
        }
        if (COMMAND_HELP.equals(text)) {
            this.sendMsg(message, SAY_HELP);
            return true;
        }
        if (COMMAND_ALL.equals(text)) {
            this.sendMsg(message, getAllProcurementsToString(procurementRepo));
            return true;
        }
        return false;
    }

    private boolean textProcessing(Message message) {
        if (message == null || !message.hasText()) {
            return false; //todo telegram exception
        }
        String text = message.getText().trim();
        ProcurementType procurementType = urlValidator.getProcurementType(text);
        if (procurementType == null) {
            this.sendMsg(message, SAY_UNKNOWN_COMMAND);
            return false;
        }
        if (procurementType == ProcurementType.LAW_615) {
            procurementService.action(ProcurementAddress.builder().address(text).build());
            this.sendMsg(message, SENT_FOR_PROCESSING);
            return true;
        }
        return false;
    }

    private String getAllProcurementsToString(final ProcurementRepo repo) {
        StringBuilder result = new StringBuilder();
        List<Procurement> procurements = repo.findAll();
        if (!procurements.isEmpty()) {
            procurements.forEach(p -> result.append(String.format(
                    STRING_FORMAT_FOR_PROCUREMENTS,
                    p.getId(),p.getUin(),p.getApplicationDeadline().toString())
            ));
        } else {
            result.append(SAY_NO_PROCUREMENTS);
        }
        return result.toString();
    }

    private void checkTempActivateKey(final Message message) {
        if (message == null || !message.hasText()) {
            return; //todo telegram exception
        }
        String text = message.getText().trim();
        if (text.startsWith(COMMAND_KEY) && text.length() < 20) {
            String [] words = text.split(SPLIT_SYMBOL_FOR_COMMAND);
            if (words.length > 1 && words[1].equals(TEMP_ACTIVATE_KEY)) {
                verifiedChatId.add(message.getChatId());
                this.sendMsg(message, SAY_SUCCESSFUL_ACTIVATION);
            } else {
                this.sendMsg(message, SAY_ERROR_ACTIVATION);
            }
        }
    }

    private boolean isVerifiedChatId(final long chatId) {
        return verifiedChatId.contains(chatId);
    }

    public void sendMessageToAll(String text) {
        if (!StringUtils.hasText(text)) {
            return;//todo telegram exception
        }
        for (Long chatId : verifiedChatId) {
            if (chatId == null) continue;
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text(text)
                    .build();
            sendMessage.enableMarkdown(true);
            try {
                this.setDefaultButtons(sendMessage);
                this.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean sendMessageToChatId(String text, long chatId) {
        if (!StringUtils.hasText(text) || chatId < 1) {
            return false;//todo telegram exception
        }
        if (!this.isVerifiedChatId(chatId)) return false;

        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(text)
                .build();
        sendMessage.enableMarkdown(true);
        try {
            this.setDefaultButtons(sendMessage);
            this.execute(sendMessage);
            return true;
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void sendMsg(final Message message, final String text) {
        if (message == null || !StringUtils.hasText(text)) {
            return;//todo telegram exception
        }

        SendMessage sendMessage = SendMessage.builder()
                .replyToMessageId(message.getMessageId())
                .chatId(message.getChatId().toString())
                .text(text)
                .build();
        sendMessage.enableMarkdown(true);

        try {
            this.setDefaultButtons(sendMessage);
            this.execute(sendMessage);
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

        keyboardFirstRow.add(new KeyboardButton(COMMAND_HELP));
        keyboardFirstRow.add(new KeyboardButton(COMMAND_ALL));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
    }

}
