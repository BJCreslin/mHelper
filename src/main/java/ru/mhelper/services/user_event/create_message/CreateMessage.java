package ru.mhelper.services.user_event.create_message;

import ru.mhelper.models.procurements.Procurement;

public interface CreateMessage {

    String createDeadLineMessage(Procurement procurement);
}
