package ru.mhelper.services.geting_code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.mhelper.models.users.User;
import ru.mhelper.repository.UserRepository;
import ru.mhelper.telegram.status_service.StatusService;

import java.time.LocalDateTime;
import java.util.*;

import static ru.mhelper.services.geting_code.ErrorGettingCode.TOO_MANY_ATTEMPTS;

@Service("TelegramCodeService")
@Primary
public class TelegramCodeServiceImpl implements TelegramCodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramCodeServiceImpl.class);

    public static final String KEY_HAS_BEEN_CREATED = "Key has been created. Attempts: {}.";
    public static final String ENTERING_THE_CODE_NUMBER = "Entering the code number: %d";
    public static final String FIND_USER = "Find user TelegramId: %d";
    public static final String CREATED_NEW_CODE_D_FOR_USER = "Created new code %d for user with TelegramId %d";
    public static final String CHECK_CODE_EXIST = "Check for code %d.";
    public static final String GET_ALL_CODES = "Get all codes: %s";
    public static final String OLD_VALUES_ARE_DELETING = "Old values are deleting.";

    private static final Map<Integer, UserIdTimed> storage = new HashMap<>();
    private static final Random random = new Random();

    private final StatusService statusService;

    private final UserRepository userRepository;


    @Value("${bot.time}")
    private int lifetime;

    @Value("${bot.max_attempts}")
    private int maxAttempts;

    public TelegramCodeServiceImpl(StatusService statusService, UserRepository userRepository) {
        this.statusService = statusService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean existByCode(Integer code) {
        removeOldValue();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format(CHECK_CODE_EXIST, code));
        }
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
            LOGGER.info(ErrorGettingCode.NO_CODES);
            throw new ErrorGettingCode(ErrorGettingCode.NO_CODES);
        }
        if (storage.containsKey(code)) {
            Long userTelegramId = storage.get(code).getUserId();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format(FIND_USER, userTelegramId));
            }
            return userTelegramId;
        }
        final String noCode = String.format(ErrorGettingCode.NO_CURRENT_CODE, code);
        LOGGER.info(noCode);
        throw new ErrorGettingCode(noCode);
    }

    @Override
    public Integer createCode(Long userTgId) {
        removeOldValue();
        int code = generateCode();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format(CREATED_NEW_CODE_D_FOR_USER, code, userTgId));
        }
        storage.put(code, new UserIdTimed(userTgId));
        var user = userRepository.findByTelegramUserId(userTgId).orElse(User.createNewTelegramUser(userTgId));
        userRepository.saveAndFlush(user);
        statusService.setGettingCodeTgStatus(user.getId());
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
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(KEY_HAS_BEEN_CREATED, count);
                }
                break;
            }
            if (count > maxAttempts) {
                throw new ErrorGettingCode(TOO_MANY_ATTEMPTS);
            }
        }
        return code;
    }

    private void removeOldValue() {
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(OLD_VALUES_ARE_DELETING);
        }
        if (!storage.isEmpty()) {
            List<Integer> codesForDelete = new ArrayList<>();
            storage.entrySet().stream().filter(x -> x.getValue().getTimeCreated().plusMinutes(lifetime).isBefore(LocalDateTime.now())).forEach(x -> codesForDelete.add(x.getKey()));
            codesForDelete.forEach(storage::remove);
        }
    }
}
