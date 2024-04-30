package ru.thelper.services.geting_code;

import java.util.Map;

public interface TelegramCodeService {

    boolean existByCode(Integer code);

    Long getTelegramUserId(Integer code);

    Integer createCode(Long userId);

    Map<Integer, UserIdTimed> getAllCodes();
}
