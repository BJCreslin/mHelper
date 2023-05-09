package ru.mhelper.services.user_event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.mhelper.models.objects.UserTextPair;
import ru.mhelper.models.procurements.Procurement;
import ru.mhelper.models.users.User;
import ru.mhelper.services.procurement.ProcurementService;
import ru.mhelper.services.user_event.create_message.CreateMessage;
import ru.mhelper.services.user_event.users_list.UsersListGet;
import ru.mhelper.telegram.BotMain;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Сервис уведомлений о событиях
 */
@Service
public class UserEventImpl implements UserEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventImpl.class);

    public static final long TIME_RANGE = 1L;

    public static final String SEND_MESSAGES_BY_TELEGRAM_BOT = "Send {} messages by TelegramBot.";

    public static final String CRON_INTERVAL = "@hourly";

    private final UsersListGet usersListGet;

    private final CreateMessage createMessage;

    private final BotMain tgBot;

    private final ProcurementService procurementService;

    public UserEventImpl(UsersListGet usersListGet, CreateMessage createMessage, BotMain tgBot, ProcurementService procurementService) {
        this.usersListGet = usersListGet;
        this.createMessage = createMessage;
        this.tgBot = tgBot;
        this.procurementService = procurementService;
    }

    @Scheduled(cron = CRON_INTERVAL)
    public void doAction() {
//        List<User> users = usersListGet.getList();
//        if (Objects.isNull(users) || users.isEmpty()) {
//            return;
//        }
//        List<UserTextPair> userTextPairs = createUserTextPairLIst(users);
//        if (!userTextPairs.isEmpty()) {
//            sendMessagesToTgBot(userTextPairs);
//        }
        List<Procurement> procurementList = getAllUpcomingEvents();

    }

    private List<Procurement> getAllUpcomingEvents() {
        LocalDateTime nextTimeEvent = getNextTimeEvent();
//        List<Procurement> result = procurementService.getAllBeforeTime(nextTimeEvent);
        return Collections.emptyList();

    }

    /**
     * Дата следующего события
     */
    private static LocalDateTime getNextTimeEvent() {
        return LocalDateTime.now().plusHours(1L);
    }

    private List<UserTextPair> createUserTextPairLIst(List<User> users) {
        final ZonedDateTime zdtNow = ZonedDateTime.now();
        List<UserTextPair> userTextPairs = new ArrayList<>();
        for (User user : users) {

//            for (Procurement procurement : user.getProcurements()) {
//                if (isEventSoon(procurement.getApplicationDeadline(), zdtNow)) {
//                    userTextPairs.add(UserTextPair.builder().user(user).text(createMessage.createDeadLineMessage(procurement)).build());
//                }
//                if (isEventSoon(procurement.getDateOfAuction(), zdtNow)) {
//                    userTextPairs.add(UserTextPair.builder().user(user).text(createMessage.createAuctionMessage(procurement)).build());
//                }
//            }
        }
        return userTextPairs;
    }

    private void sendMessagesToTgBot(List<UserTextPair> userTextPairs) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(SEND_MESSAGES_BY_TELEGRAM_BOT, userTextPairs.size());
        }
        if (!userTextPairs.isEmpty()) {
            for (UserTextPair pair : userTextPairs) {
                tgBot.sendMessageToUser(pair.getUser(), pair.getText());
            }
        }
    }

    private boolean isEventSoon(ZonedDateTime procurement, ZonedDateTime zdtNow) {
        return procurement.isAfter(zdtNow) &&
            procurement.isBefore(zdtNow.minusHours(TIME_RANGE));
    }
}
