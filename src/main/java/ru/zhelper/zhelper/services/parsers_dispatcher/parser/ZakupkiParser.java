package ru.zhelper.zhelper.services.parsers_dispatcher.parser;

import ru.zhelper.zhelper.models.Procurement;

public interface ZakupkiParser {
    Procurement parse(String html);
}
