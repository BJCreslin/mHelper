package ru.thelper.services.user_event.create_message;

import ru.thelper.models.procurements.Procurement;

public interface CreateMessage {

    String createDeadLineMessage(Procurement procurement);

    String createAuctionMessage(Procurement procurement);
}
