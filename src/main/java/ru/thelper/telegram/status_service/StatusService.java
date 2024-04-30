package ru.thelper.telegram.status_service;

public interface StatusService {

    void setGettingCodeTgStatus(Long userId);

    void setNoStatusTgStatus(Long userId);

    void setNewTelegramUserTgStatus(Long userId);
}
