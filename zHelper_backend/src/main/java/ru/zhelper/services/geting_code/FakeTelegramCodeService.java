package ru.zhelper.services.geting_code;

import org.springframework.stereotype.Service;
import ru.zhelper.services.exceptions.BadRequest;

import java.util.HashMap;
import java.util.Map;

import static ru.zhelper.services.exceptions.BadRequest.CODE_NOT_FOUND;

@Service
public class FakeTelegramCodeService implements TelegramCodeService {
    private final Map<Integer, String> codeMap;

    public FakeTelegramCodeService() {
        codeMap = new HashMap<>();
        codeMap.put(100, "telegramUser1");
        codeMap.put(200, "telegramUser2");
    }

    @Override
    public boolean existByCode(Integer code) {
        return codeMap.containsKey(code);
    }

    @Override
    public String getTelegramUserId(Integer code) {
        if (existByCode(code)) {
            return codeMap.get(code);
        }
        throw new BadRequest(CODE_NOT_FOUND);
    }
}