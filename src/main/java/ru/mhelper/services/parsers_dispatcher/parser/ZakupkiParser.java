package ru.mhelper.services.parsers_dispatcher.parser;

import ru.mhelper.models.procurements.Procurement;

public interface ZakupkiParser {
    Procurement parse(String html);
}
