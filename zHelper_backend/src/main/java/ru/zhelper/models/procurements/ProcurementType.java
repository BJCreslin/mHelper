package ru.zhelper.models.procurements;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ProcurementType {
    LAW_44("Закупки по 44-ФЗ"),
    LAW_223("Закупки по 223-ФЗ"),
    LAW_615("Закупки по 615-ФЗ");

    private final String title;
    private static final Map<String, ProcurementType> ENUM_MAP;

    ProcurementType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }

    static {
        Map<String, ProcurementType> map = new ConcurrentHashMap<>();
        for (ProcurementType instance : ProcurementType.values()) {
            map.put(instance.getTitle().toLowerCase(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static ProcurementType get(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }
}
