package ru.thelper.services.user_event.create_message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.thelper.models.procurements.Procurement;

import java.time.format.DateTimeFormatter;

@Service
public class CreateMessageImpl implements CreateMessage {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateMessageImpl.class);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final String CREATE_DEAD_LINE_MESSAGE_FOR_PROCUREMENT_WITH_UIN = " Create deadLine message for procurement with Uin {}.";

    public static final String CREATE_AUCTION_MESSAGE_FOR_PROCUREMENT_WITH_UIN = "Create auction message for procurement with Uin {}.";

    @Override
    public String createDeadLineMessage(Procurement procurement) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(CREATE_DEAD_LINE_MESSAGE_FOR_PROCUREMENT_WITH_UIN, procurement.getUin());
        }
        return String.format("Наступает DeadLine в %s для закупки %s UIN %s. Адрес: %s"
                , procurement.getApplicationDeadline().format(formatter)
                , procurement.getObjectOf().substring(0, 50)
                , procurement.getUin()
                , procurement.getLinkOnPlacement());
    }

    @Override
    public String createAuctionMessage(Procurement procurement) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(CREATE_AUCTION_MESSAGE_FOR_PROCUREMENT_WITH_UIN, procurement.getUin());
        }
        return String.format("Наступает время аукциона в %s для закупки %s UIN %s. Адрес: %s"
                , procurement.getDateOfAuction().format(formatter)
                , procurement.getObjectOf().substring(0, 50)
                , procurement.getUin()
                , procurement.getLinkOnPlacement());
    }
}
