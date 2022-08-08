package ru.mhelper.services.user_event.users_list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mhelper.models.user_procurement.UserProcurementLinks;
import ru.mhelper.models.users.User;
import ru.mhelper.repository.UserProcurementLinksRepository;
import ru.mhelper.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
public class UsersListGetImpl implements UsersListGet {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersListGetImpl.class);

    private final UserRepository userRepository;

    private final UserProcurementLinksRepository userProcurementLinksRepository;

    public static final String NO_ANY_USERS_WITH_PROCUREMENTS_AND_TELEGRAM_ID = "No any users with procurements and telegramID";

    public static final String FOUND_USERS_WITH_PROCUREMENTS_AND_TELEGRAM_ID = "Found users {} with procurements and telegramID";

    public static final String GETTING_USERS_LIST = "Getting users list.";

    public UsersListGetImpl(UserRepository userRepository, UserProcurementLinksRepository userProcurementLinksRepository) {
        this.userRepository = userRepository;
        this.userProcurementLinksRepository = userProcurementLinksRepository;
    }

    @Override
    public List<User> getList() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(GETTING_USERS_LIST);
        }
        return getUsersWithProcurementsAndTelegramId();
    }

    private List<User> getUsersWithProcurementsAndTelegramId() {
        List<User> users = userProcurementLinksRepository.findAll().stream().map(UserProcurementLinks::getUser).toList();

        if (users.isEmpty()) {
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
