package ru.zhelper.services.geting_code;

public interface TelegramCodeService {
    boolean existByCode(Integer code);

    String getTelegramUserId(Integer code);
}
