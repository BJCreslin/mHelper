package ru.mhelper.services.telegram.actions.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mhelper.models.users.User;
import ru.mhelper.repository.UserRepository;

import java.util.Optional;

import static ru.mhelper.services.telegram.actions.text.HelloTelegramTextAction.SERVICE_ACTION_NAME;

@Service(SERVICE_ACTION_NAME)
public class HelloTelegramTextAction implements TelegramTextAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloTelegramTextAction.class);

    public static final String SERVICE_ACTION_NAME = "hello";

    public static final String HELLO_FROM_USER_WITH_CHAT_ID = "Hello from user with chatId {}.";

    private final UserRepository repository;

    public HelloTelegramTextAction(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public String action(Long chatId, String text) {
        StringBuilder result = new StringBuilder("Hello there");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(HELLO_FROM_USER_WITH_CHAT_ID, chatId);
        }
        if (repository.existsByTelegramUserId(chatId)) {
            Optional<User> user = repository.findByTelegramUserId(chatId);
            user.ifPresent(value -> result.append(" ").append(value.getUsername()));
        }
        result.append(".");
        return result.toString();
    }

    @Override
    public String getInfo() {
        return  " Привет.";
    }
}
