package ru.thelper.telegram.status_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.thelper.models.users.TelegramStateType;
import ru.thelper.models.users.User;
import ru.thelper.repository.UserRepository;

@Service
public class StatusServiceImpl implements StatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusServiceImpl.class);

    public static final String SET_TG_STATUS = "TG status set {} for User with id {}.";

    private final UserRepository userRepository;

    public StatusServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void setGettingCodeTgStatus(Long userId) {
        TelegramStateType status = TelegramStateType.GETTING_CODE;
        setTgStatusForUser(userId, status);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(SET_TG_STATUS, status, userId);
        }
    }

    @Override
    public void setNoStatusTgStatus(Long userId) {
        TelegramStateType status = TelegramStateType.NO_STATEMENT;
        setTgStatusForUser(userId, status);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(SET_TG_STATUS, status, userId);
        }
    }

    @Override
    public void setNewTelegramUserTgStatus(Long userId) {
        TelegramStateType status = TelegramStateType.NEW_TELEGRAM_USER;
        setTgStatusForUser(userId, status);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(SET_TG_STATUS, status, userId);
        }
    }

    private void setTgStatusForUser(Long userId, TelegramStateType status) {
        User user = userRepository.getReferenceById(userId);
        user.setTelegramStateType(status);
        userRepository.save(user);
    }
}
