package ru.mhelper.telegram.actions.answer_services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mhelper.models.users.User;
import ru.mhelper.repository.UserRepository;

import java.util.Optional;

import static ru.mhelper.telegram.actions.answer_services.HelloTelegramTextAnswer.SERVICE_ACTION_NAME;

@Service(SERVICE_ACTION_NAME)
public class HelloTelegramTextAnswer implements TelegramTextAnswer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloTelegramTextAnswer.class);

    public static final String SERVICE_ACTION_NAME = "hello";

    public static final String HELLO_FROM_USER_WITH_CHAT_ID = "Hello from user with chatId {}.";

    private final UserRepository repository;

    public HelloTelegramTextAnswer(UserRepository repository) {
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
