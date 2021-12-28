package ru.zhelper.services.parsers_dispatcher;

import ru.zhelper.models.procurements.Procurement;

public interface Dispatcher {
    Procurement getFromUrl(String url);
}
