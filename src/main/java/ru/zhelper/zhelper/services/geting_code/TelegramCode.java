package ru.zhelper.zhelper.services.geting_code;

public interface TelegramCode {
    boolean existByCode(Integer code);

    String getTelegramUserId(Integer code);
}
