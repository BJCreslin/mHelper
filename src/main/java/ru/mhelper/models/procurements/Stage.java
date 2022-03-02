package ru.mhelper.models.procurements;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Stage {
    SUBMISSION_OF_APPLICATION("Подача заявок"),
    WORK_OF_THE_COMMISSION("Работа комиссии"),
    PROCUREMENT_ENDED("Процедура завершена"),
    PROCUREMENT_CANCELED("Процедура отменена");

    private final String title;
    private static final Map<String, Stage> ENUM_MAP;

    static {
        Map<String, Stage> map = new ConcurrentHashMap<>();
        for (Stage instance : Stage.values()) {
            map.put(instance.getTitle().toLowerCase(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static Stage get(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }

    public String getTitle() {
        return title;
    }

    Stage(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
