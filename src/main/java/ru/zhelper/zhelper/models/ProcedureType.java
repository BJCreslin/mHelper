package ru.zhelper.zhelper.models;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ProcedureType {
    ELECTRONIC_AUCTION("Электронный аукцион"),
    PURCHASE_FROM_A_SINGLE_SUPPLIER("Закупка у единственного поставщика (подрядчика, исполнителя)"),
    REQUEST_FOR_QUOTATIONS_IN_ELECTRONIC_FORM("Запрос котировок в электронной форме"),
    METHOD_FOR_DETERMINING_THE_SUPPLIER_BY_ARTICLE_111_44_FZ("Способ определения поставщика (подрядчика, исполнителя), " +
            "установленный Правительством Российской Федерации в соответствии со ст. 111 Федерального закона № 44-ФЗ"),
    OPEN_ELECTRONIC_COMPETITION("Открытый конкурс в электронной форме"),
    CLOSED_AUCTION("Закрытый аукцион"),
    REQUEST_FOR_PROPOSALS_IN_ELECTRONIC_FORM("Запрос предложений в электронной форме"),
    PRELIMINARY_SELECTION("Предварительный отбор"),
    COMPETITION_WITH_LIMITED_PARTICIPATION_IN_ELECTRONIC_FORM("Конкурс с ограниченным участием в электронной форме"),
    REQUEST_FOR_QUOTATIONS("Запрос котировок"),
    OPEN_COMPETITION("Открытый конкурс"),
    CLOSED_COMPETITION("Закрытый конкурс"),
    TWO_STAGE_ELECTRONIC_COMPETITION("Двухэтапный конкурс в электронной форме"),
    CLOSED_COMPETITION_WITH_LIMITED_PARTICIPATION("Закрытый конкурс с ограниченным участием"),
    CLOSED_TWO_STAGE_COMPETITION("Закрытый двухэтапный конкурс"),
    COMPETITION_WITH_LIMITED_PARTICIPATION("Конкурс с ограниченным участием"),
    TWO_STAGE_COMPETITION("Двухэтапный конкурс"),
    REQUEST_FOR_QUOTATIONS_WITHOUT_PLACING_A_NOTICE("Запрос котировок без размещения извещения"),
    REQUEST_FOR_PROPOSALS("Запрос предложений"),
    PURCHASE_OF_GOODS_FROM_A_SINGLE_SUPPLIER("Закупка товара у единственного поставщика на сумму," +
            " предусмотренную частью 12 статьи 93 Закона № 44-ФЗ"),
    ELECTRONIC_AUCTION_615FZ("Электронный аукцион (ПП РФ 615)"),
    PRELIMINARY_SELECTION_615FZ("Предварительный отбор (ПП РФ 615)"),
    DEFAULT_NONAME_PROCEDURE("Неопределенный тип закупки");

    private final String title;
    private static final Map<String, ProcedureType> ENUM_MAP;

    public String getTitle() {
        return title;
    }

    ProcedureType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

    static {
        Map<String, ProcedureType> map = new ConcurrentHashMap<>();
        for (ProcedureType instance : ProcedureType.values()) {
            map.put(instance.getTitle().toLowerCase(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static ProcedureType get(String name) {
        return ENUM_MAP.getOrDefault(name.toLowerCase(), DEFAULT_NONAME_PROCEDURE);
    }
}
