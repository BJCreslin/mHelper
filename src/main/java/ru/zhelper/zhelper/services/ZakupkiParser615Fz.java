package ru.zhelper.zhelper.services;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.zhelper.zhelper.models.ProcedureType;
import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.models.Stage;
import ru.zhelper.zhelper.services.exceptions.BadDataParsingException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ZakupkiParser615Fz implements ZakupkiParser {
    Logger logger = LoggerFactory.getLogger(ZakupkiParser615Fz.class);
    private static final String UIN_SELECTOR = "span[class=navBreadcrumb__text]";
    private static final String STAGE_SELECTOR = "span[class=cardMainInfo__state]";
    private static final String DEADLINE_SELECTOR = "span[class=section__title]:contains(Дата и время окончания срока подачи заявок на участие в электронном аукционе)";
    private static final String CONTRACT_PRICE_SELECTOR = "span[class=section__title]:contains(Начальная (максимальная) цена договора, рублей)";
    private static final String METHOD_SELECTOR = "span[class=section__title]:contains(Способ определения поставщика (подрядчика, исполнителя, подрядной организации))";
    private static final String PUBLISHER_SELECTOR = "span[class=section__title]:contains(Наименование организации)";
    private static final String NUMBER_TO_REPLACE = "№ ";
    private static final String REPLACEMENT = "";
    private static final String START_LAW_TO_REPLACE = "ПП РФ ";
    private static final String SPAN_SEPARATOR = " <span";
    private static final String NBSP = "nbsp";
    private static final String IN_RUSSIAN_ROUBLE = " в российских рублях";
    private static final String DOT = ".";
    private static final String COMMA = ",";
    private static final String FINISH_LAW_TO_REPLACE = " Электронный аукцион на оказание услуг или выполнение работ по капитальному ремонту общего имущества в многоквартирном доме";
    private static final String METHOD = "Способ определения поставщика";
    private static final String BAD_DATA_EXCEPTION = "Bad data in {}.";
    private static final String PUBLISHER_NAME = "Организатор.";
    private static final String UIN = "uin";
    private static final String STAGE = "stage";
    private static final String FZ_NUMBER = "Fz number";
    private static final String APPLICATION_DEADLINE = "APPLICATION_DEADLINE";
    private static final String CONTRACT_PRICE = "CONTRACT PRICE";
    private static final String PARSING = "Starting parse from html. Size {}";
    private static final String PARSED = "Procurement {} was parsed.";
    private static final String DATA_FORMAT = "dd.MM.yyy";
    private static final String TIME_FORMATTER = "dd.MM.yyy";
    private static final String DATE_TIME_FORMATTER = "dd.MM.yyyy HH:mm";

    @Override
    public Procurement parse(String html) {
        if (logger.isDebugEnabled()) {
            logger.debug(PARSING, html.length());
        }
        Procurement procurement = new Procurement();
        procurement.setUin(getUin(html));
        procurement.setStage(getStage(html));
        procurement.setFzNumber(getFzNumber(html));
        procurement.setApplicationDeadline(getApplicationDeadline(html));
        procurement.setContractPrice(getContractPrice(html));
        procurement.setProcedureType(getProcedureType(html));
        procurement.setPublisherName(getPublisherName(html));
//todo ---------------->Insert other parse information <------------------------
        if (logger.isDebugEnabled()) {
            logger.debug(PARSED, procurement);
        }
        return procurement;
    }

    protected String getPublisherName(String html) {
        try {
            return Jsoup.parse(html).body().select(PUBLISHER_SELECTOR).first().siblingElements().first().text();

        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, PUBLISHER_NAME, exception);
            throw new BadDataParsingException(PUBLISHER_NAME, exception);
        }
    }

    protected ProcedureType getProcedureType(String html) {
        try {
            String procedureType = Jsoup.parse(html).body().select(METHOD_SELECTOR).first().siblingElements().first().text();
            return ProcedureType.get(procedureType);
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, METHOD, exception);
            throw new BadDataParsingException(METHOD, exception);
        }
    }

    protected BigDecimal getContractPrice(String html) {
        try {
            String textContractPrice = Jsoup.parse(html).body().select(CONTRACT_PRICE_SELECTOR).first().siblingElements().first().text().
                    replace(NBSP, REPLACEMENT).replace(IN_RUSSIAN_ROUBLE, REPLACEMENT).replace(" ", "").replace(COMMA, DOT);
            return new BigDecimal(textContractPrice);
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, CONTRACT_PRICE, exception);
            throw new BadDataParsingException(CONTRACT_PRICE, exception);
        }
    }

    protected LocalDateTime getApplicationDeadline(String html) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER);
        try {
            String textDate = Jsoup.parse(html).body().select(DEADLINE_SELECTOR).
                    first().siblingElements().first().html().split(SPAN_SEPARATOR)[0];
            return LocalDateTime.parse(textDate, formatter);
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, APPLICATION_DEADLINE, exception);
            throw new BadDataParsingException(BAD_DATA_EXCEPTION, exception);
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
