package ru.zhelper.zhelper.services.geting_code;

public interface TelegramCodeService {
    boolean existByCode(Integer code);

    Long getTelegramUserId(Integer code);

    Integer createCode(Long userId);
}
