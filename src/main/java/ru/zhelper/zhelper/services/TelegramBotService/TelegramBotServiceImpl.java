package ru.zhelper.zhelper.services.TelegramBotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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
import ru.zhelper.zhelper.repository.ProcurementRepo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class TelegramBotServiceImpl extends TelegramLongPollingBot implements TelegramBotService {

    private static final String TEMP_ACTIVATE_KEY = "a12345a";
    private static final String COMMAND_HELP = "/help";
    private static final String COMMAND_ALL = "/all";
    private static final String COMMAND_KEY = "/key";
    private static final String SAY_HELP = "Я ваш помощник в закупках, всегда рад напомнить о ближайщих " +
            "закрывающихся позициях!";
    private static final String SAY_UNKNOWN_COMMAND = "Неизвестная команда! Введите " + COMMAND_HELP +
            " для получения справки.";
    private static final String SAY_SUCCESSFUL_ACTIVATION =  "Успешная активация!";
    private static final String SAY_ERROR_ACTIVATION =  "Ошибка ввода ключа активации, введите ещё раз!";
    private static final String SAY_ENTER_ACTIVATION_KEY = "Введите ключ активации: " + COMMAND_KEY + " <ключ>";
    private static final String SPLIT_SYMBOL_FOR_COMMAND = "\\s";
    private static final String STRING_FORMAT_FOR_PROCUREMENTS = "id:%d uin:%s date:%s %n";
    private static final String SAY_NO_PROCUREMENTS = "Нет закупок!";

    private final HashSet<Long> verifiedUsers = new HashSet<>();

    private final String username;
    private final String token;
    private final ProcurementRepo procurementRepo;

    @Autowired
    public TelegramBotServiceImpl(@Value("${telegram.bot.username}") String username,
                                  @Value("${telegram.bot.token}") String token,
                                  ProcurementRepo procurementRepo) {
        this.username = username;
        this.token = token;
        this.procurementRepo = procurementRepo;
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
        if (!this.checkTempActivateKey(message)) return;
        this.commandProcessing(message);
    }

    private void commandProcessing(final Message message) {
        if (message == null || !message.hasText()) {
            return; //todo telegram exception
        }
        String text = message.getText().trim();
        if (text.startsWith(COMMAND_KEY)) {
            return;
        }
        if (COMMAND_HELP.equals(text)) {
            this.sendMsg(message, SAY_HELP);
            return;
        }
        if (COMMAND_ALL.equals(text)) {
            this.sendMsg(message, getAllProcurementsToString(procurementRepo));
            return;
        }
        this.sendMsg(message, SAY_UNKNOWN_COMMAND);
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

    private boolean checkTempActivateKey(final Message message) {
        if (message == null || !message.hasText()) {
            return false; //todo telegram exception
        }
        String text = message.getText().trim();
        if (text.startsWith(COMMAND_KEY) && text.length() < 20) {
            String [] words = text.split(SPLIT_SYMBOL_FOR_COMMAND);
            if (words.length > 1 && words[1].equals(TEMP_ACTIVATE_KEY)) {
                verifiedUsers.add(message.getFrom().getId());
                this.sendMsg(message, SAY_SUCCESSFUL_ACTIVATION);
                return true;
            } else {
                this.sendMsg(message, SAY_ERROR_ACTIVATION);
                return false;
            }
        }
        if (!this.checkUsers(message.getFrom().getId())) {
            this.sendMsg(message, SAY_ENTER_ACTIVATION_KEY);
            return false;
        }
        return true;
    }

    private boolean checkUsers(final Long userId) {
        return verifiedUsers.contains(userId);
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
            this.setButtons(sendMessage);
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void setButtons(final SendMessage sendMessage) {
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
