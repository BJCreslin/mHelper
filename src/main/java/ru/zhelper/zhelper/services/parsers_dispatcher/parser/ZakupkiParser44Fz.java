package ru.zhelper.zhelper.services.parsers_dispatcher.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.zhelper.zhelper.models.ProcedureType;
import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.models.Stage;
import ru.zhelper.zhelper.services.exceptions.BadDataParsingException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service("Law44")
public class ZakupkiParser44Fz implements ZakupkiParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZakupkiParser44Fz.class);
    private static final String STAGE_SELECTOR = "span[class=cardMainInfo__state distancedText]";
    private static final String UIN_SELECTOR = "span[class=navBreadcrumb__text]";
    private static final String FZ_NUMBER_SELECTOR = "div[class=cardMainInfo__title d-flex text-truncate]";
    private static final String CONTRACT_PRICE_SELECTOR = "span[class=cardMainInfo__content cost]";
    private static final String PROCEDURE_TYPE_SELECTOR = "span[class=section__title]:contains(Способ определения поставщика)";
    private static final String PUBLISHER_NAME_SELECTOR = "span[class=section__title]:contains(Организация, осуществляющая размещение)";
    private static final String RESTRICTIONS_SELECTOR = "span[class=section__title]:contains(Требования к участникам)";
    private static final String LINK_ON_PLACEMENT_SELECTOR = "span[class=section__title]:contains(Адрес электронной площадки в информационно-телекоммуникационной сети \"Интернет\")";
    private static final String APPLICATION_SECURE_SELECTOR = "span[class=section__title]:contains(Размер обеспечения заявки)";
    private static final String CONTRACT_SECURE_SELECTOR = "span[class=section__title]:contains(Размер обеспечения исполнения контракта)";
    private static final String OBJECT_OF_SELECTOR = "span[class=section__title]:contains(Наименование объекта закупки)";
    private static final String APPLICATION_DEADLINE_SELECTOR = "span[class=section__title]:contains(Дата и время окончания срока подачи заявок)";
    private static final String LAST_UPDATED_FROM_EIS_SELECTOR = "span[class=cardMainInfo__title]:contains(Размещено)";
    private static final String DATE_TIME_LAST_UPDATED_SELECTOR = "span[class=cardMainInfo__title]:contains(Обновлено)";
    private static final String DATE_TIME_FORMATTER = "dd.MM.yyyy HH:mm";
    private static final String DATE_FORMATTER = "dd.MM.yyyy";
    private static final String TIME_ZONE_SELECTOR = "div[class=time-zone__value]";
    private static final String BAD_DATA_EXCEPTION = "Bad data in method {}.";
    private static final String PARSING = "Starting parse from html. Size {}";
    private static final String PARSED = "Procurement {} was parsed.";
    private static final String CONNECTING_WRONG = "Jsoup connect to url is bad in method {}";
    private static final String METHOD_PARSE = "parse";
    private static final String METHOD_GET_STAGE = "getStage";
    private static final String METHOD_GET_UIN = "getUin";
    private static final String METHOD_GET_FZNUMBER = "getFzNumber";
    private static final String METHOD_GET_CONTRACT_PRICE = "getContractPrice";
    private static final String METHOD_GET_PROCEDURE_TYPE = "getProcedureType";
    private static final String METHOD_GET_PUBLISHER_NAME = "getPublisherName";
    private static final String METHOD_GET_RESTRICTIONS = "getRestrictions";
    private static final String METHOD_GET_LINK_OF_PLACEMENT = "getLinkOnPlacement";
    private static final String METHOD_GET_APPLICATION_SECURE = "getApplicationSecure";
    private static final String METHOD_GET_CONTRACT_SECURE = "getContractSecure";
    private static final String METHOD_GET_OBJECT_OF = "getObjectOf";
    private static final String METHOD_GET_APPLICATION_DEADLINE = "getApplicationDeadline";
    private static final String METHOD_GET_LAST_UPDATED_FROM_EIS = "getLastUpdatedFromEIS";
    private static final String METHOD_GET_DATE_TIME_LAST_UPDATED = "getDateTimeLastUpdated";
    private static final String METHOD_GET_TIME_ZONE = "getTimeZone";
    private static final String REPLACEMENT_SYMBOL_RUBLE = "₽";
    private static final String REPLACEMENT_NBSP = "&nbsp;";
    private static final String REPLACEMENT_NUMBER_TO_REPLACE = "№ ";
    private static final String REPLACEMENT_EMPTY = "";
    private static final String REPLACEMENT_SPACE = " ";
    private static final String REPLACEMENT_COMMA = ",";
    private static final String REPLACEMENT_DOT = ".";
    private static final String REPLACEMENT_SOME_SYMBOLS = "&#8381";
    private static final String REPLACEMENT_BR = "<br>";
    private static final String REPLACEMENT_RUSSIAN_RUBLE = "Российский рубль";
    private static final String OPEN_PARENTHESIS = "(";
    private static final String CLOSE_PARENTHESIS = ")";
    private static final String SELECTOR_A = "a";

    @Override
    public Procurement parse(String url) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(PARSING, url);
        }

        Document htmlFile = null;

        try {
            htmlFile = Jsoup.connect(url).get();
        } catch (IOException exception) {
            LOGGER.error(CONNECTING_WRONG, METHOD_PARSE, exception);
            exception.printStackTrace();
        }

        Procurement procurement = new Procurement();
        procurement.setStage(getStage(htmlFile));
        procurement.setUin(getUin(htmlFile));
        procurement.setFzNumber(getFzNumber(htmlFile));
        procurement.setApplicationDeadline(getApplicationDeadline(htmlFile));
        procurement.setContractPrice(getContractPrice(htmlFile));
        procurement.setProcedureType(getProcedureType(htmlFile));
        procurement.setPublisherName(getPublisherName(htmlFile));
        procurement.setRestrictions(getRestrictions(htmlFile));
        procurement.setLinkOnPlacement(getLinkOnPlacement(htmlFile));
        procurement.setApplicationSecure(getApplicationSecure(htmlFile));
        procurement.setContractSecure(getContractSecure(htmlFile));
        procurement.setObjectOf(getObjectOf(htmlFile));
        procurement.setLastUpdatedFromEIS(getLastUpdatedFromEIS(htmlFile));
        procurement.setDateTimeLastUpdated(getDateTimeLastUpdated(htmlFile));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(PARSED, procurement);
        }

        return procurement;
    }

    protected Stage getStage(Document htmlFile) {
        try {
            return Stage.get(htmlFile.select(STAGE_SELECTOR).first().text());
        } catch (NullPointerException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_STAGE, exception);
            throw new BadDataParsingException(METHOD_GET_STAGE, exception);
        }
    }

    protected String getUin(Document htmlFile) {
        try {
            return htmlFile.select(UIN_SELECTOR).first().text().replace(REPLACEMENT_NUMBER_TO_REPLACE, REPLACEMENT_EMPTY);
        } catch (NullPointerException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_UIN, exception);
            throw new BadDataParsingException(METHOD_GET_UIN, exception);
        }
    }

    protected int getFzNumber(Document htmlFile) {
        try {
            return Integer.parseInt(htmlFile.select(FZ_NUMBER_SELECTOR).first().text().substring(0,2));
        } catch (NullPointerException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_FZNUMBER, exception);
            throw new BadDataParsingException(METHOD_GET_FZNUMBER, exception);
        }
    }

    protected BigDecimal getContractPrice(Document htmlFile) {
        try {
            return new BigDecimal(htmlFile.select(CONTRACT_PRICE_SELECTOR).first().text()
                    .replace(REPLACEMENT_SOME_SYMBOLS, REPLACEMENT_EMPTY)
                    .replace(REPLACEMENT_SYMBOL_RUBLE,REPLACEMENT_EMPTY)
                    .replace(REPLACEMENT_SPACE, REPLACEMENT_EMPTY)
                    .replace(REPLACEMENT_COMMA,REPLACEMENT_DOT));
        } catch (NullPointerException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_CONTRACT_PRICE, exception);
            throw new BadDataParsingException(METHOD_GET_CONTRACT_PRICE, exception);
        }
    }

    protected ProcedureType getProcedureType(Document htmlFile) {
        try {
            return ProcedureType.get(htmlFile.select(PROCEDURE_TYPE_SELECTOR).first().siblingElements().first().text());
        } catch (NullPointerException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_PROCEDURE_TYPE, exception);
            throw new BadDataParsingException(METHOD_GET_PROCEDURE_TYPE, exception);
        }
    }

    protected String getPublisherName(Document htmlFile) {
        try {
            return htmlFile.select(PUBLISHER_NAME_SELECTOR).first().siblingElements().html();
        } catch (NullPointerException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_PUBLISHER_NAME, exception);
            throw new BadDataParsingException(METHOD_GET_PUBLISHER_NAME, exception);
        }
    }

    protected String getRestrictions(Document htmlFile) {
        try {
            return htmlFile.select(RESTRICTIONS_SELECTOR).first().siblingElements().first().text()
                    .replace(REPLACEMENT_NBSP,REPLACEMENT_EMPTY)
                    .replace(REPLACEMENT_BR,REPLACEMENT_EMPTY)
                    .trim();
        } catch (NullPointerException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_RESTRICTIONS, exception);
            throw new BadDataParsingException(METHOD_GET_RESTRICTIONS, exception);
        }
    }

    protected URL getLinkOnPlacement(Document htmlFile) {
        try {
            return new URL(htmlFile.select(LINK_ON_PLACEMENT_SELECTOR).first().siblingElements().select(SELECTOR_A).html());
        } catch (NullPointerException | MalformedURLException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_LINK_OF_PLACEMENT, exception);
            throw new BadDataParsingException(METHOD_GET_LINK_OF_PLACEMENT, exception);
        }
    }

    protected String getApplicationSecure(Document htmlFile) {
        try {
            return htmlFile.select(APPLICATION_SECURE_SELECTOR).first().siblingElements().first().text()
                    .replace(REPLACEMENT_NBSP, REPLACEMENT_EMPTY)
                    .replace(REPLACEMENT_RUSSIAN_RUBLE,REPLACEMENT_EMPTY)
                    .replace(REPLACEMENT_COMMA, REPLACEMENT_DOT)
                    .replace(REPLACEMENT_SPACE, REPLACEMENT_EMPTY);
        } catch (NullPointerException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_APPLICATION_SECURE, exception);
            throw new BadDataParsingException(METHOD_GET_APPLICATION_SECURE, exception);
        }
    }

    protected String getContractSecure(Document htmlFile) {
        try {
            return htmlFile.select(CONTRACT_SECURE_SELECTOR).first().siblingElements().first().text()
                    .replace(REPLACEMENT_NBSP, REPLACEMENT_EMPTY)
                    .replace(REPLACEMENT_RUSSIAN_RUBLE, REPLACEMENT_EMPTY)
                    .replace(REPLACEMENT_COMMA, REPLACEMENT_DOT)
                    .replace(REPLACEMENT_SPACE, REPLACEMENT_EMPTY)
                    .replace(REPLACEMENT_SYMBOL_RUBLE, REPLACEMENT_EMPTY);
        } catch (NullPointerException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_CONTRACT_SECURE, exception);
            throw new BadDataParsingException(METHOD_GET_CONTRACT_SECURE, exception);
        }
    }

    protected String getObjectOf(Document htmlFile) {
        try {
            return htmlFile.select(OBJECT_OF_SELECTOR).first().siblingElements().first().text();
        } catch (NullPointerException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_OBJECT_OF, exception);
            throw new BadDataParsingException(METHOD_GET_OBJECT_OF, exception);
        }
    }

    protected ZonedDateTime getApplicationDeadline(Document htmlFile) {
        try {
            String datetime = htmlFile.select(APPLICATION_DEADLINE_SELECTOR).first().siblingElements().first().text().substring(0,16);
            DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER);
            ZoneId zoneId = ZoneId.of(getTimeZone(htmlFile));
            return ZonedDateTime.of(LocalDateTime.parse(datetime, format), zoneId);
        } catch (NullPointerException | DateTimeParseException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_APPLICATION_DEADLINE, exception);
            throw new BadDataParsingException(METHOD_GET_APPLICATION_DEADLINE, exception);
        }
    }

    protected LocalDate getLastUpdatedFromEIS(Document htmlFile) {
        try {
            String temp = htmlFile.select(LAST_UPDATED_FROM_EIS_SELECTOR).first().siblingElements().first().text();
            return LocalDate.parse(temp, DateTimeFormatter.ofPattern(DATE_FORMATTER));
        } catch (NullPointerException | DateTimeParseException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_LAST_UPDATED_FROM_EIS, exception);
            throw new BadDataParsingException(METHOD_GET_LAST_UPDATED_FROM_EIS, exception);
        }
    }

    protected LocalDate getDateTimeLastUpdated(Document htmlFile) {
        try {
            String temp = htmlFile.select(DATE_TIME_LAST_UPDATED_SELECTOR).first().siblingElements().first().text();
            return LocalDate.parse(temp, DateTimeFormatter.ofPattern(DATE_FORMATTER));
        } catch (NullPointerException | DateTimeParseException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_DATE_TIME_LAST_UPDATED, exception);
            throw new BadDataParsingException(METHOD_GET_DATE_TIME_LAST_UPDATED, exception);
        }
    }

    protected String getTimeZone(Document htmlFile) {
        try {
            String temp = htmlFile.select(TIME_ZONE_SELECTOR).first().text();
            return temp.substring(temp.indexOf(OPEN_PARENTHESIS)+1, temp.indexOf(CLOSE_PARENTHESIS));
        } catch (NullPointerException | DateTimeParseException exception) {
            LOGGER.error(BAD_DATA_EXCEPTION, METHOD_GET_TIME_ZONE, exception);
            throw new BadDataParsingException(METHOD_GET_TIME_ZONE, exception);
        }
    }

}
