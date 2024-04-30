package ru.thelper.services.parsers_dispatcher;

import ru.thelper.models.procurements.Procurement;

public interface Dispatcher {
    Procurement getFromUrl(String url);
}
