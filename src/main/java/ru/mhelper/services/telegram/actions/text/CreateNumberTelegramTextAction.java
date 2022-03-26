package ru.mhelper.services.telegram.actions.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mhelper.services.geting_code.ErrorGettingCode;
import ru.mhelper.services.geting_code.TelegramCodeService;

import static ru.mhelper.services.telegram.actions.text.CreateNumberTelegramTextAction.SERVICE_ACTION_NAME;

@Service(SERVICE_ACTION_NAME)
public class CreateNumberTelegramTextAction implements TelegramTextAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateNumberTelegramTextAction.class);

    public static final String SERVICE_ACTION_NAME="number";

    private static final String NUMBER_MESSAGE = "Полученный код: ";

    private static final String NUMBER_ERROR = "Ошибка получения кода. Попробуйте повторить позже.";

    public static final String RECEIVING_CODE_FOR_CHAT_ID_CODE = "Receiving code for chatId:{}. Code: {}.";

    private final TelegramCodeService telegramCodeService;

    public CreateNumberTelegramTextAction(TelegramCodeService telegramCodeService) {
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
        return " Команда получения кода для входа в систему.";
    }
}
