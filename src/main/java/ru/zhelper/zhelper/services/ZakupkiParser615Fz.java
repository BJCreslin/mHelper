package ru.zhelper.zhelper.services;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.models.Stage;
import ru.zhelper.zhelper.services.exceptions.BadDataParsingException;

public class ZakupkiParser615Fz implements ZakupkiParser {
    Logger logger = LoggerFactory.getLogger(ZakupkiParser615Fz.class);
    private static final String UIN_SELECTOR = "span[class=navBreadcrumb__text]";
    private static final String STAGE_SELECTOR = "span[class=cardMainInfo__state]";
    private static final String NUMBER_TO_REPLACE = "№ ";
    private static final String REPLACEMENT = "";

    private static final String BAD_DATA_EXCEPTION = "Bad data in {}.";
    private static final String UIN = "uin";
    private static final String STAGE = "stage";
    private static final String PARSING = "Starting parse from html. Size {}";
    private static final String PARSED = "Procurement {} was parsed.";

    @Override
    public Procurement parse(String html) {
        if (logger.isDebugEnabled()) {
            logger.debug(PARSING, html.length());
        }
        Procurement procurement = new Procurement();
        procurement.setUin(getUin(html));
        procurement.setStage(getStage(html));
//todo ---------------->Insert other parse information <------------------------
        if (logger.isDebugEnabled()) {
            logger.debug(PARSED, procurement);
        }
        return procurement;
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
            return Stage.valueOf(Jsoup.parse(html).body().select(STAGE_SELECTOR).first().html());
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, STAGE, exception);
            throw new BadDataParsingException(BAD_DATA_EXCEPTION, exception);
        }
    }
}
