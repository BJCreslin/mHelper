package ru.zhelper.zhelper.services;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.models.Stage;
import ru.zhelper.zhelper.services.exceptions.BadDataParsingException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ZakupkiParser615Fz implements ZakupkiParser {
    Logger logger = LoggerFactory.getLogger(ZakupkiParser615Fz.class);
    private static final String UIN_SELECTOR = "span[class=navBreadcrumb__text]";
    private static final String STAGE_SELECTOR = "span[class=cardMainInfo__state]";
    private static final String NUMBER_TO_REPLACE = "№ ";
    private static final String REPLACEMENT = "";
    private static final String START_LAW_TO_REPLACE = "ПП РФ ";
    private static final String FINISH_LAW_TO_REPLACE = " Электронный аукцион на оказание услуг или выполнение работ по капитальному ремонту общего имущества в многоквартирном доме";

    private static final String BAD_DATA_EXCEPTION = "Bad data in {}.";
    private static final String UIN = "uin";
    private static final String STAGE = "stage";
    private static final String FZ_NUMBER = "Fz number";
    private static final String APPLICATION_DEADLINE = "APPLICATION_DEADLINE";
    private static final String PARSING = "Starting parse from html. Size {}";
    private static final String PARSED = "Procurement {} was parsed.";
    private static final String DATA_FORMAT = "dd.MM.yyy";
    private static final String TIME_FORMATTER = "dd.MM.yyy";

    @Override
    public Procurement parse(String html) {
        if (logger.isDebugEnabled()) {
            logger.debug(PARSING, html.length());
        }
        Procurement procurement = new Procurement();
        procurement.setUin(getUin(html));
        procurement.setStage(getStage(html));
        procurement.setFzNumber(getFzNumber(html));
        // procurement.setApplicationDeadline(getApplicationDeadline(html));

//todo ---------------->Insert other parse information <------------------------
        if (logger.isDebugEnabled()) {
            logger.debug(PARSED, procurement);
        }
        return procurement;
    }

    protected String getApplicationDeadline(String html) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATA_FORMAT);
        //  DateTimeFormatter timeFormatter=DateTimeFormatter.ofPattern(TIME_FORMATTER);
        try {
            LocalDate date = LocalDate.parse(Jsoup.parse(html).body().select("div[class=date]").first().
                    select("span[class=cardMainInfo__content]").last().html(), formatter);
            // LocalTime time=LocalTime.parse(Jsoup.parse(html).body().select("div[class=date]"))
            return Jsoup.parse(html).body().select("div[class=container]").
                    next().next().
                    select("section[class=blockInfo__section section]").
                    select("span[class=section__info]").first().html();
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, APPLICATION_DEADLINE, exception);
            throw new BadDataParsingException(APPLICATION_DEADLINE, exception);
        }
    }

    protected int getFzNumber(String html) {
        try {
            return Integer.parseInt(Jsoup.parse(html).body().select("div[class=cardMainInfo__title d-flex text-truncate]").first().text().
                    replace(START_LAW_TO_REPLACE, REPLACEMENT).replace(FINISH_LAW_TO_REPLACE, REPLACEMENT));
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, FZ_NUMBER, exception);
            throw new BadDataParsingException(BAD_DATA_EXCEPTION, exception);
        }
    }

    protected String getUin(String html) {
        try {
            return Jsoup.parse(html).body().select(UIN_SELECTOR).first().html().replace(NUMBER_TO_REPLACE, REPLACEMENT);
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, UIN, exception);
            throw new BadDataParsingException(BAD_DATA_EXCEPTION, exception);
        }
    }

    protected Stage getStage(String html) {
        try {
            return Stage.get(Jsoup.parse(html).body().select(STAGE_SELECTOR).first().html());
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, STAGE, exception);
            throw new BadDataParsingException(BAD_DATA_EXCEPTION, exception);
        }
    }
}
