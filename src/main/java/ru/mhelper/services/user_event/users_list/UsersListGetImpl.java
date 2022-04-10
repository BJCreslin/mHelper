package ru.mhelper.services.user_event.users_list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mhelper.models.users.User;
import ru.mhelper.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class UsersListGetImpl implements UsersListGet {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersListGetImpl.class);

    private final UserRepository userRepository;

    public static final String NO_ANY_USERS_WITH_PROCUREMENTS_AND_TELEGRAM_ID = "No any users with procurements and telegramID";

    public static final String FOUND_USERS_WITH_PROCUREMENTS_AND_TELEGRAM_ID = "Found users {} with procurements and telegramID";

    public static final String GETTING_USERS_LIST = "Getting users list.";

    public UsersListGetImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getList() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(GETTING_USERS_LIST);
        }
        return getUsersWithProcurementsAndTelegramId();
    }

    private List<User> getUsersWithProcurementsAndTelegramId() {
        List<User> users = userRepository.findUsersByProcurementsIsNotNullAndTelegramUserIdIsNotNull();
        if (Objects.isNull(users) || users.isEmpty()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(NO_ANY_USERS_WITH_PROCUREMENTS_AND_TELEGRAM_ID);
            }
            return Collections.emptyList();
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(FOUND_USERS_WITH_PROCUREMENTS_AND_TELEGRAM_ID, users.size());
        }
        return users;
    }
}
