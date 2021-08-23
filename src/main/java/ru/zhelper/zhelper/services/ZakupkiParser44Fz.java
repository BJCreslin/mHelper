package ru.zhelper.zhelper.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class ZakupkiParser44Fz implements ZakupkiParser {
    Logger logger = LoggerFactory.getLogger(ZakupkiParser44Fz.class);
    private static final String STAGE_SELECTOR = "span[class=cardMainInfo__state distancedText]";
    private static final String UIN_SELECTOR = "span[class=navBreadcrumb__text]";
    private static final String FZ_NUMBER_SELECTOR = "div[class=cardMainInfo__title d-flex text-truncate]";
    private static final String CONTRACT_PRICE_SELECTOR = "span[class=cardMainInfo__content cost]";
    private static final String PROCEDURE_TYPE_SELECTOR = "span[class=section__title]:contains(Способ определения поставщика)";
    private static final String PUBLISHER_NAME_SELECTOR = "span[class=section__title]:contains(Организация, осуществляющая размещение)";
    private static final String RESTRICTIONS_SELECTOR = "span[class=requirements_participants_block]";
    private static final String LINK_ON_PLACEMENT_SELECTOR = "span[class=section__title]:contains(Адрес электронной площадки в информационно-телекоммуникационной сети \"Интернет\")";
    private static final String APPLICATION_SECURE_SELECTOR = "span[class=section__title]:contains(Размер обеспечения заявки)";
    private static final String CONTRACT_SECURE_SELECTOR = "span[class=section__title]:contains(Размер обеспечения исполнения контракта)";
    private static final String OBJECT_OF_SELECTOR = "span[class=section__title]:contains(Наименование объекта закупки)";
    private static final String APPLICATION_DEADLINE_SELECTOR = "span[class=section__title]:contains(Дата и время окончания срока подачи заявок)";
    private static final String LAST_UPDATED_FROM_EIS_SELECTOR = "span[class=cardMainInfo__title]:contains(Размещено)";
    private static final String DATE_TIME_LAST_UPDATED_SELECTOR = "span[class=cardMainInfo__title]:contains(Обновлено)";
    private static final String BAD_DATA_EXCEPTION = "Bad data in {}.";
    private static final String DATE_TIME_FORMATTER = "dd.MM.yyyy HH:mm";
    private static final String DATE_FORMATTER = "dd.MM.yyyy";
    private static final String TIME_ZONE_SELECTOR = "div[class=time-zone__value]";
    private static final String PARSING = "Starting parse from html. Size {}";
    private static final String PARSED = "Procurement {} was parsed.";

    @Override
    public Procurement parse(String url) {
        if (logger.isDebugEnabled()) {
            logger.debug(PARSING, url);
        }

        Document htmlFile = null;

        try {
            htmlFile = Jsoup.connect(url).get();
        } catch (IOException exception) {
            logger.error("Jsoup connect to url is bad", "parse", exception);
            exception.printStackTrace();
        }

        /*Stage stage = getStage(htmlFile);
        String uin = getUin(htmlFile);
        String fzNumber = getFzNumber(htmlFile);
        ZonedDateTime applicationDeadline = getApplicationDeadline(htmlFile);
        BigDecimal contractPrice = getContractPrice(htmlFile);
        ProcedureType procedureType = getProcedureType(htmlFile);
        String publisherName = getPublisherName(htmlFile);
        String restrictions = getRestrictions(htmlFile);
        URL linkOnPlacement = getLinkOnPlacement(htmlFile);
        String applicationSecure = getApplicationSecure(htmlFile);
        String contractSecure = getContractSecure(htmlFile);
        String objectOf = getObjectOf(htmlFile);
        LocalDate lastUpdatedFromEIS = getLastUpdatedFromEIS(htmlFile);
        LocalDate dateTimeLastUpdated = getDateTimeLastUpdated(htmlFile);*/

        return null;
        /*Procurement procurement = new Procurement();
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

        if (logger.isDebugEnabled()) {
            logger.debug(PARSED, procurement);
        }

        return procurement;*/
    }

    Stage getStage(Document htmlFile) {
        try {
            return Stage.get(htmlFile.select(STAGE_SELECTOR).html());
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getStage", exception);
            throw new BadDataParsingException("getStage", exception);
        }
    }

    String getUin(Document htmlFile) {
        try {
            return htmlFile.select(UIN_SELECTOR).html().replace("№ ", "");//next().html().replace("№ ", "");
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getUin", exception);
            throw new BadDataParsingException("getUin", exception);
        }
    }

    String getFzNumber(Document htmlFile) {
        try {
            return htmlFile.select(FZ_NUMBER_SELECTOR).first().text().substring(0,2);//next().html().replace("№ ", "");
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getFzNumber", exception);
            throw new BadDataParsingException("getFzNumber", exception);
        }
    }

    BigDecimal getContractPrice(Document htmlFile) {
        try {
            return new BigDecimal(htmlFile.select(CONTRACT_PRICE_SELECTOR).first().text()
                    .replace("&#8381", "")
                    .replace("₽","")
                    .replace(" ", "")
                    .replace(",","."));
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getContractPrice", exception);
            throw new BadDataParsingException("getContractPrice", exception);
        }
    }

    ProcedureType getProcedureType(Document htmlFile) {
        try {
            return ProcedureType.get(htmlFile.select(PROCEDURE_TYPE_SELECTOR).first().siblingElements().first().text());
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getProcedureType", exception);
            throw new BadDataParsingException("getProcedureType", exception);
        }
    }

    String getPublisherName(Document htmlFile) {
        try {
            return htmlFile.select(PUBLISHER_NAME_SELECTOR).first().siblingElements().html();
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getPublisherName", exception);
            throw new BadDataParsingException("getPublisherName", exception);
        }
    }

    String getRestrictions(Document htmlFile) {
        try {
            return htmlFile.select(RESTRICTIONS_SELECTOR).html().replaceAll("&nbsp;","").replaceAll("<br>","").trim();
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getRestrictions", exception);
            throw new BadDataParsingException("getRestrictions", exception);
        }
    }

    URL getLinkOnPlacement(Document htmlFile) {
        try {
            return new URL(htmlFile.select(LINK_ON_PLACEMENT_SELECTOR).first().siblingElements().select("a").html());
        } catch (NullPointerException | MalformedURLException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getLinkOnPlacement", exception);
            throw new BadDataParsingException("getLinkOnPlacement", exception);
        }
    }

    String getApplicationSecure(Document htmlFile) {
        try {
            return htmlFile.select(APPLICATION_SECURE_SELECTOR).first().siblingElements().first().text()
                    .replace("&nbsp;", "")
                    .replace("Российский рубль","")
                    .replace(",", ".")
                    .replace(" ", "");
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getApplicationSecure", exception);
            throw new BadDataParsingException("getApplicationSecure", exception);
        }
    }

    String getContractSecure(Document htmlFile) {
        try {
            return htmlFile.select(CONTRACT_SECURE_SELECTOR).first().siblingElements().first().text()
                    .replace("&nbsp;", "")
                    .replace("Российский рубль","")
                    .replace(",", ".")
                    .replace(" ", "")
                    .replace("₽", "");
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getContractSecure", exception);
            throw new BadDataParsingException("getContractSecure", exception);
        }
    }

    String getObjectOf(Document htmlFile) {
        try {
            return htmlFile.select(OBJECT_OF_SELECTOR).first().siblingElements().first().text();
        } catch (NullPointerException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getObjectOf", exception);
            throw new BadDataParsingException("getObjectOf", exception);
        }
    }

    ZonedDateTime getApplicationDeadline(Document htmlFile) {
        try {
            String datetime = htmlFile.select(APPLICATION_DEADLINE_SELECTOR).first().siblingElements().first().text().substring(0,16);
            //String datetime = temp.substring(0,16);
            DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER);
            ZoneId zoneId = ZoneId.of(getTimeZone(htmlFile));
            ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.parse(datetime, format), zoneId);
            return zonedDateTime;
        } catch (NullPointerException | DateTimeParseException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getApplicationDeadline", exception);
            throw new BadDataParsingException("getApplicationDeadline", exception);
        }
    }

    LocalDate getLastUpdatedFromEIS(Document htmlFile) {
        try {
            String temp = htmlFile.select(LAST_UPDATED_FROM_EIS_SELECTOR).first().siblingElements().first().text();
            return LocalDate.parse(temp, DateTimeFormatter.ofPattern(DATE_FORMATTER));
        } catch (NullPointerException | DateTimeParseException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getLastUpdatedFromEIS", exception);
            throw new BadDataParsingException("getLastUpdatedFromEIS", exception);
        }
    }

    LocalDate getDateTimeLastUpdated(Document htmlFile) {
        try {
            String temp = htmlFile.select(DATE_TIME_LAST_UPDATED_SELECTOR).first().siblingElements().first().text();
            return LocalDate.parse(temp, DateTimeFormatter.ofPattern(DATE_FORMATTER));
        } catch (NullPointerException | DateTimeParseException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getDateTimeLastUpdated", exception);
            throw new BadDataParsingException("getDateTimeLastUpdated", exception);
        }
    }

    String getTimeZone(Document htmlFile) {
        try {
            String temp = htmlFile.select(TIME_ZONE_SELECTOR).first().text();
            return temp.substring(temp.indexOf("(")+1, temp.indexOf(")"));
        } catch (NullPointerException | DateTimeParseException exception) {
            logger.error(BAD_DATA_EXCEPTION, "getTimeZone", exception);
            throw new BadDataParsingException("getTimeZone", exception);
        }
    }

}
