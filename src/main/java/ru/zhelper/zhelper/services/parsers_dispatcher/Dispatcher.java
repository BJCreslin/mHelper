package ru.zhelper.zhelper.services.parsers_dispatcher;

import ru.zhelper.zhelper.models.Procurement;

public interface Dispatcher {
    Procurement getFromUrl(String url);
}
