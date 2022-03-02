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
import ru.mhelper.services.geting_code.ErrorGettingCode;
import ru.mhelper.services.geting_code.TelegramCodeService;

import static ru.mhelper.services.telegram.TelegramBotServiceException.ERROR_SEND_MESSAGE;

@Service
public class Bot extends TelegramLongPollingBot {
    private final Logger LOGGER = LoggerFactory.getLogger(Bot.class);

    private final static String NUMBER_MESSAGE = "Полученный код: ";
    private final static String NUMBER_ERROR = "Ошибка получения кода. Попробуйте повторить позже.";

    private final static String NUMBER_OPERATION = "number";
    private final static String START_OPERATION = "/start";

    @Value("${bot.name}")
    private String BOT_NAME;

    @Value("${bot.token}")
    private String BOT_TOKEN;

    private final TelegramCodeService telegramCodeService;

    public Bot(TelegramCodeService telegramCodeService) {
        this.telegramCodeService = telegramCodeService;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
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
                    text=NUMBER_ERROR;
                }
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
}
