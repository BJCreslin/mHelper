package ru.thelper.services.parsers_dispatcher.parser;

import ru.thelper.models.procurements.Procurement;

public interface ZakupkiParser {
    Procurement parse(String html);
}
