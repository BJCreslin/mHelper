package ru.mhelper.services.user_event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.mhelper.models.objects.UserTextPair;
import ru.mhelper.models.procurements.Procurement;
import ru.mhelper.models.users.User;
import ru.mhelper.services.telegram.Bot;
import ru.mhelper.services.user_event.create_message.CreateMessage;
import ru.mhelper.services.user_event.users_list.UsersListGet;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserEventImpl implements UserEvent {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventImpl.class);

    public static final String CREATE_DEAD_LINE_MESSAGE_FOR_USER_WITH_ID = "Create deadLine message for user with Id {}.";

    public static final String CREATE_AUCTION_MESSAGE_FOR_USER_WITH_ID = "Create auction message for user with Id {}.";

    private final UsersListGet usersListGet;

    private final CreateMessage createMessage;

    private final Bot tgBot;

    public UserEventImpl(UsersListGet usersListGet, CreateMessage createMessage, Bot tgBot) {
        this.usersListGet = usersListGet;
        this.createMessage = createMessage;
        this.tgBot = tgBot;
    }

    @Scheduled(cron = "@hourly")
    public void doAction() {
        List<User> users = usersListGet.getList();
        if (Objects.isNull(users) || users.isEmpty()) {
            return;
        }
        ZonedDateTime zdtNow = ZonedDateTime.now();
        List<UserTextPair> userTextPairs = new ArrayList<>();
        for (User user : users) {
            for (Procurement procurement : user.getProcurements()) {
                if (procurement.getApplicationDeadline().isAfter(zdtNow) &&
                        procurement.getApplicationDeadline().isBefore(zdtNow.minusHours(1L))) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(CREATE_DEAD_LINE_MESSAGE_FOR_USER_WITH_ID, user.getId());
                    }
                    userTextPairs.add(UserTextPair.builder().user(user).text(createMessage.createDeadLineMessage(procurement)).build());
                }
                if (procurement.getDateOfAuction().isAfter(zdtNow) &&
                        procurement.getDateOfAuction().isBefore(zdtNow.minusHours(1L))) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(CREATE_AUCTION_MESSAGE_FOR_USER_WITH_ID, user.getId());
                    }
                    userTextPairs.add(UserTextPair.builder().user(user).text(createMessage.createAuctionMessage(procurement)).build());
                }
            }
        }
        if (!userTextPairs.isEmpty()) {
            for (UserTextPair pair : userTextPairs) {
                tgBot.sendMessageToUser(pair.getUser(), pair.getText());
            }
        }
    }
}
