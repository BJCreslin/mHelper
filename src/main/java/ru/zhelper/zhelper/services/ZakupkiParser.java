package ru.zhelper.zhelper.services;

import ru.zhelper.zhelper.models.Procurement;

public interface ZakupkiParser {
    Procurement parse(String html);
}
