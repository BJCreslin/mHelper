package ru.zhelper.zhelper.services.TelegramBotService;


import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum TelegramCommandsEnum {

    HELP("/help"),
    ALL("/all"),
    KEY("/key");

    private final String title;
    private static final Map<String, TelegramCommandsEnum> ENUM_MAP;

    public String getTitle() {
        return title;
    }

    TelegramCommandsEnum(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    static {
        Map<String, TelegramCommandsEnum> map = new ConcurrentHashMap<>();
        for (TelegramCommandsEnum instance : TelegramCommandsEnum.values()) {
            map.put(instance.getTitle().toLowerCase(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }
}
