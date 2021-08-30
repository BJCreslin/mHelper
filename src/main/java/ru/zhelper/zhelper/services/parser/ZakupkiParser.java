package ru.zhelper.zhelper.services.parser;

import ru.zhelper.zhelper.models.Procurement;

public interface ZakupkiParser {
    Procurement parse(String html);
}
