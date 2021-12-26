package ru.zhelper.services.parsers_dispatcher.parser;

import ru.zhelper.models.procurements.Procurement;

public interface ZakupkiParser {
    Procurement parse(String html);
}
