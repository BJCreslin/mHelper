package ru.zhelper.zhelper.services.geting_code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static ru.zhelper.zhelper.services.geting_code.ErrorGettingCode.TOO_MANY_ATTEMPTS;

@Service("TelegramCodeService")
@Primary
public class TelegramCodeServiceImpl implements TelegramCodeService {
    private final static Logger LOGGER = LoggerFactory.getLogger(TelegramCodeServiceImpl.class);

    private final static Map<Integer, UserIdTimed> storage = new HashMap<>();

    private final static Random random = new Random();
    public static final String ENTERING_THE_CODE_NUMBER = "Entering the code number: %d";
    public static final String FIND_USER = "Find user: %d";
    public static final String CREATED_NEW_CODE_D_FOR_USER = "Created new code %d for user %d";

    @Value("${bot.time}")
    private int ACTION_TIME;

    @Value("${bot.max_attempts}")
    private int MAX_ATTEMPTS;

    @Override
    public boolean existByCode(Integer code) {
        removeOldValue();
        if (storage.isEmpty()) {
            return false;
        }
        return storage.containsKey(code);
    }

    @Override
    public Long getTelegramUserId(Integer code) {
        removeOldValue();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format(ENTERING_THE_CODE_NUMBER, code));
        }
        if (storage.isEmpty()) {
            return null;
        }
        if (storage.containsKey(code)) {
            Long userId = storage.get(code).getUserId();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format(FIND_USER, userId));
            }
            return userId;
        }
        return null;
    }

    @Override
    public Integer createCode(Long userId) {
        removeOldValue();
        int code = generateCode();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format(CREATED_NEW_CODE_D_FOR_USER, code, userId));
        }
        storage.put(code, new UserIdTimed(userId));
        return code;
    }

    private int generateCode() {
        int count = 0;
        int code;
        while (true) {
            code = random.nextInt(1000000);
            count++;
            if (!storage.containsKey(code)) {
                break;
            }
            if (count > MAX_ATTEMPTS) {
                throw new ErrorGettingCode(TOO_MANY_ATTEMPTS);
            }
        }
        return code;
    }

    private void removeOldValue() {
        storage.entrySet().stream().filter(x -> x.getValue().getTimeCreated().plusMinutes(ACTION_TIME).isBefore(LocalTime.now())).forEach(x -> storage.remove(x.getKey()));
    }
}
