package ru.mhelper.services.telegram.actions.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("start")
public class StartTelegramTextAction implements TelegramTextAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartTelegramTextAction.class);

    public static final String START_FOR_USER_WITH_ID = "Start for user with id: {}";

    public static final String INFO_MESSAGE = "Start bot with getting the enter code.";

    private final CreateNumberTelegramTextAction numberTelegramTextAction;

    public StartTelegramTextAction(CreateNumberTelegramTextAction numberTelegramTextAction) {
        this.numberTelegramTextAction = numberTelegramTextAction;
    }

    @Override
    public String action(Long chatId, String text) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(START_FOR_USER_WITH_ID, chatId);
        }
        return numberTelegramTextAction.action(chatId, text);
    }

    @Override
    public String getInfo() {
        return INFO_MESSAGE;
    }
}
