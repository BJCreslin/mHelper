package ru.mhelper.telegram.actions.answer_services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("start")
public class StartTelegramTextAnswer implements TelegramTextAnswer {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartTelegramTextAnswer.class);

    public static final String START_FOR_USER_WITH_ID = "Start for user with id: {}";

    public static final String INFO_MESSAGE = "Start bot with getting the enter code.";

    private final CreateNumberTelegramTextAnswer numberTelegramTextAction;

    public StartTelegramTextAnswer(CreateNumberTelegramTextAnswer numberTelegramTextAction) {
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
