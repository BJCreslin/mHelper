package ru.mhelper.services.geting_code;

import org.springframework.stereotype.Service;
import ru.mhelper.services.exceptions.BadRequest;

import java.util.HashMap;
import java.util.Map;

@Service
public class FakeTelegramCodeService implements TelegramCodeService {
    private final Map<Integer, Long> codeMap;

    public FakeTelegramCodeService() {
        codeMap = new HashMap<>();
        codeMap.put(100, 100000L);
        codeMap.put(200, 200000L);
    }

    @Override
    public boolean existByCode(Integer code) {
        return codeMap.containsKey(code);
    }

    @Override
    public Long getTelegramUserId(Integer code) {
        if (existByCode(code)) {
            return codeMap.get(code);
        }
        throw new BadRequest(BadRequest.CODE_NOT_FOUND);
    }

    @Override
    public Integer createCode(Long userId) {
        return 100;
    }
}
