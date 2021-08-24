package ru.zhelper.zhelper.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.zhelper.zhelper.models.ProcedureType;
import ru.zhelper.zhelper.models.Stage;
import ru.zhelper.zhelper.services.exceptions.BadDataParsingException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class ZakupkiParser44FzTest {
    private ZakupkiParser44Fz parser44Fz;
    private Document goodHTML;
    private Document badHTML;

    @BeforeEach
    void setUp() throws URISyntaxException, IOException {
        parser44Fz = new ZakupkiParser44Fz();
        goodHTML = Jsoup.parse(new File(this.getClass().getClassLoader().getResource("44fz0834100000221000038.html").toURI()), null);
        badHTML = Jsoup.parse(new File(this.getClass().getClassLoader().getResource("44fz0834100000221000038bad.html").toURI()), null);
    }

    @Test
    void givenHtml_whenGetStage_getStage() {
        Stage stage = Stage.get("Подача заявок");
        Assertions.assertEquals(stage, parser44Fz.getStage(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetStage_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getStage(badHTML));
    }

    @Test
    void givenHtml_whenGetUin_getUin() {
        Assertions.assertEquals("0834100000221000038", parser44Fz.getUin(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetUin_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getUin(badHTML));
    }

    @Test
    void givenHtml_whenGetFzNumber_getFzNumber() {
        Assertions.assertEquals(44, parser44Fz.getFzNumber(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetFzNumber_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getFzNumber(badHTML));
    }

    @Test
    void givenHtml_whenGetContractPrice_getContractPrice() {
        BigDecimal contractPrice = new BigDecimal("2809884.00");
        Assertions.assertEquals(contractPrice, parser44Fz.getContractPrice(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetContractPrice_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getContractPrice(badHTML));
    }

    @Test
    void givenHtml_whenGetProcedureType_getProcedureType() {
        ProcedureType procedureType = ProcedureType.get("Электронный аукцион");
        Assertions.assertEquals(procedureType, parser44Fz.getProcedureType(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetProcedureType_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getProcedureType(badHTML));
    }

    @Test
    void givenHtml_whenGetPublisherName_getPublisherName() {
        String publisherName = "ФЕДЕРАЛЬНОЕ КАЗЕННОЕ УЧРЕЖДЕНИЕ \"ЦЕНТР ХОЗЯЙСТВЕННОГО И СЕРВИСНОГО ОБЕСПЕЧЕНИЯ ГЛАВНОГО УПРАВЛЕНИЯ МИНИСТЕРСТВА ВНУТРЕННИХ ДЕЛ РОССИЙСКОЙ ФЕДЕРАЦИИ ПО ИРКУТСКОЙ ОБЛАСТИ\"";
        Assertions.assertEquals(publisherName, parser44Fz.getPublisherName(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetPublisherName_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getPublisherName(badHTML));
    }

    @Test
    void givenHtml_whenGetRestrictions_getRestrictions() {
        String restrictions = "1 Единые требования к участникам закупок в соответствии с ч. 1 ст. 31 Закона № 44-ФЗ дополнительная информация к требованию отсутствует " +
                "2 Требования к участникам закупок в соответствии с частью 1.1 статьи 31 Федерального закона № 44-ФЗ дополнительная информация к требованию отсутствует";
        Assertions.assertEquals(restrictions, parser44Fz.getRestrictions(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetRestrictions_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getRestrictions(badHTML));
    }

    @Test
    void givenHtml_whenGetLinkOnPlacement_getLinkOnPlacement() {
        Assertions.assertEquals("http://roseltorg.ru", parser44Fz.getLinkOnPlacement(goodHTML).toString());
    }

    @Test
    void givenBadHtml_whenGetLinkOnPlacement_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getLinkOnPlacement(badHTML));
    }

    @Test
    void givenHtml_whenGetApplicationSecure_getApplicationSecure() {
        String applicationSecure = "28098.84";
        Assertions.assertEquals(applicationSecure, parser44Fz.getApplicationSecure(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetApplicationSecure_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getApplicationSecure(badHTML));
    }

    @Test
    void givenHtml_whenGetContractSecure_getContractSecure() {
        String contractSecure = "140494.20(5%)";
        Assertions.assertEquals(contractSecure, parser44Fz.getContractSecure(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetContractSecure_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getContractSecure(badHTML));
    }

    @Test
    void givenHtml_whenGetObjectOf_getObjectOf() {
        String objectOf = "Поставка шин пневматических для легковых автомобилей (Запасные части для ремонта к автотранспорту в рамках ГОЗ, в целях обеспечения ГПВ)";
        Assertions.assertEquals(objectOf, parser44Fz.getObjectOf(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetObjectOf_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getApplicationDeadline(badHTML));
    }

    @Test
    void givenHtml_whenGetApplicationDeadline_getApplicationDeadline() {
        String datetime = "30.08.2021 09:00";
        DateTimeFormatter format = DateTimeFormatter.ofPattern(parser44Fz.DATE_TIME_FORMATTER);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.parse(datetime, format), ZoneId.of("UTC+8"));
        Assertions.assertEquals(zonedDateTime, parser44Fz.getApplicationDeadline(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetApplicationDead_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getApplicationDeadline(badHTML));
    }

    @Test
    void givenHtml_whenGetLastUpdatedFromEIS_getLastUpdatedFromEIS() {
        LocalDate tempLd = LocalDate.parse("20.08.2021", DateTimeFormatter.ofPattern(parser44Fz.DATE_FORMATTER));
        Assertions.assertEquals(tempLd, parser44Fz.getLastUpdatedFromEIS(goodHTML));

    }

    @Test
    void givenBadHtml_whenGetLastUpdatedFromEIS_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getLastUpdatedFromEIS(badHTML));
    }

    @Test
    void givenHtml_whenGetDateTimeLastUpdated_getDateTimeLastUpdated() {
        LocalDate tempLd = LocalDate.parse("20.08.2021", DateTimeFormatter.ofPattern(parser44Fz.DATE_FORMATTER));
        Assertions.assertEquals(tempLd, parser44Fz.getDateTimeLastUpdated(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetDateTimeLastUpdated_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getDateTimeLastUpdated(badHTML));
    }

    @Test
    void givenHtml_whenGetTimeZone_getTimeZone() {
        Assertions.assertEquals("UTC+8", parser44Fz.getTimeZone(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetTimeZone_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getTimeZone(badHTML));
    }
}