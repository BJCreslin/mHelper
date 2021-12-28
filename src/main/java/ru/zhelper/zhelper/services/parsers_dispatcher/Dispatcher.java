package ru.zhelper.zhelper.services.parsers_dispatcher;

import ru.zhelper.zhelper.models.procurements.Procurement;

public interface Dispatcher {
    Procurement getFromUrl(String url);
}
