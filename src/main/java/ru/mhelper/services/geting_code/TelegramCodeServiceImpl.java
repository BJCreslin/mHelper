package ru.mhelper.services.geting_code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static ru.mhelper.services.geting_code.ErrorGettingCode.TOO_MANY_ATTEMPTS;

@Service("TelegramCodeService")
@Primary
public class TelegramCodeServiceImpl implements TelegramCodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramCodeServiceImpl.class);

    private static final Map<Integer, UserIdTimed> storage = new HashMap<>();

    private static final Random random = new Random();

    public static final String ENTERING_THE_CODE_NUMBER = "Entering the code number: %d";

    public static final String FIND_USER = "Find user: %d";

    public static final String CREATED_NEW_CODE_D_FOR_USER = "Created new code %d for user %d";

    public static final String CHECK_CODE_EXIST = "Check for code %d. %b";

    public static final String GET_ALL_CODES = "Get all codes: %s";

    @Value("${bot.time}")
    private int lifetime;

    @Value("${bot.max_attempts}")
    private int maxAttempts;

    @Override
    public boolean existByCode(Integer code) {
        removeOldValue();
        final boolean exist = storage.isEmpty();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format(CHECK_CODE_EXIST, code, exist));
        }
        if (exist) {
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
            LOGGER.info(ErrorGettingCode.NO_CODES);
            throw new ErrorGettingCode(ErrorGettingCode.NO_CODES);
        }
        if (storage.containsKey(code)) {
            Long userId = storage.get(code).getUserId();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format(FIND_USER, userId));
            }
            return userId;
        }
        final String noCode = String.format(ErrorGettingCode.NO_CURRENT_CODE, code);
        LOGGER.info(noCode);
        throw new ErrorGettingCode(noCode);
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


    public Map<Integer, UserIdTimed> getAllCodes() {
        removeOldValue();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format(GET_ALL_CODES, storageToString()));
        }
        return storage;
    }

    public String storageToString() {
        StringBuilder text = new StringBuilder();
        if (storage.isEmpty()) {
            return "";
        }
        storage.forEach((x, y) -> text.append("{").append(x.intValue()).append(", ").append(y.getUserId()).append(", ").append(y.getTimeCreated()).append("}").append("\n"));
        return text.toString();
    }

    private int generateCode() {
        int count = 0;
        int code;
        while (true) {
            code = random.nextInt(900000) + 100000;
            count++;
            if (!storage.containsKey(code)) {
                break;
            }
            if (count > maxAttempts) {
                throw new ErrorGettingCode(TOO_MANY_ATTEMPTS);
            }
        }
        return code;
    }

    private void removeOldValue() {
        if (!storage.isEmpty()) {
            storage.entrySet().stream().filter(x -> x.getValue().getTimeCreated().plusMinutes(lifetime).isBefore(LocalTime.now())).forEach(x -> storage.remove(x.getKey()));
        }
    }
}
