package ru.mhelper.services.user_event.create_message;

import org.springframework.stereotype.Service;
import ru.mhelper.models.procurements.Procurement;

import java.time.format.DateTimeFormatter;

@Service
public class CreateMessageImpl implements CreateMessage {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public String createDeadLineMessage(Procurement procurement) {
        return String.format("Наступает DeadLine в %s для закупки %s UIN %s. Адрес: %s"
                , procurement.getApplicationDeadline().format(formatter)
                , procurement.getObjectOf().substring(0, 50)
                , procurement.getUin()
                , procurement.getLinkOnPlacement());
    }

    @Override
    public String createAuctionMessage(Procurement procurement) {
        return String.format("Наступает время аукциона в %s для закупки %s UIN %s. Адрес: %s"
                , procurement.getDateOfAuction().format(formatter)
                , procurement.getObjectOf().substring(0, 50)
                , procurement.getUin()
                , procurement.getLinkOnPlacement());
    }
}
