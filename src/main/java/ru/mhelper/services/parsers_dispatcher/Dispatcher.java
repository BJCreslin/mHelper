package ru.mhelper.services.parsers_dispatcher;

import ru.mhelper.models.procurements.Procurement;

public interface Dispatcher {
    Procurement getFromUrl(String url);
}
