package ru.thelper.telegram.actions.answer_services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.thelper.services.geting_code.ErrorGettingCode;
import ru.thelper.services.geting_code.TelegramCodeService;

import static ru.thelper.telegram.actions.answer_services.CreateNumberTelegramTextAnswer.SERVICE_ACTION_NAME;

@Service(SERVICE_ACTION_NAME)
public class CreateNumberTelegramTextAnswer implements TelegramTextAnswer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateNumberTelegramTextAnswer.class);

    public static final String SERVICE_ACTION_NAME="number";

    private static final String NUMBER_MESSAGE = "Полученный код: ";

    private static final String NUMBER_ERROR = "Ошибка получения кода. Попробуйте повторить позже.";

    public static final String RECEIVING_CODE_FOR_CHAT_ID_CODE = "Receiving code for chatId:{}. Code: {}.";

    public static final String GET_INFO = "Команда получения кода для входа в систему.";

    private final TelegramCodeService telegramCodeService;

    public CreateNumberTelegramTextAnswer(TelegramCodeService telegramCodeService) {
        this.telegramCodeService = telegramCodeService;
    }

    @Override
    public String action(Long chatId, String text) {
        String result;
        int gettingNumber=-1;
        try {
            gettingNumber = telegramCodeService.createCode(chatId);
            result = NUMBER_MESSAGE + gettingNumber;
        } catch (ErrorGettingCode e) {
            LOGGER.error(ErrorGettingCode.TOO_MANY_ATTEMPTS);
            result = NUMBER_ERROR;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(RECEIVING_CODE_FOR_CHAT_ID_CODE, chatId, gettingNumber);
        }
        return result;
    }

    @Override
    public String getInfo() {
        return GET_INFO;
    }
}
