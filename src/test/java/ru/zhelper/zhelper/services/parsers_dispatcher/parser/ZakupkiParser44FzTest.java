package ru.zhelper.zhelper.services.parsers_dispatcher.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.zhelper.zhelper.models.procurements.ProcedureType;
import ru.zhelper.zhelper.models.procurements.Stage;
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

@SpringBootTest
@ActiveProfiles("test")
class ZakupkiParser44FzTest {
    private static final String FILE_NAME_GOOD_HTML = "44fz0834100000221000038.html";
    private static final String FILE_NAME_BAD_HTML = "44fz0834100000221000038bad.html";
    private static final String TEST_STAGE = "Подача заявок";
    private static final String TEST_UIN = "0834100000221000038";
    private static final int TEST_FZ_NUMBER = 44;
    private static final String TEST_CONTRACT_PRICE = "2809884.00";
    private static final String TEST_PROCEDURE_TYPE = "Электронный аукцион";
    private static final String TEST_PUBLISHER_NAME = "ФЕДЕРАЛЬНОЕ КАЗЕННОЕ УЧРЕЖДЕНИЕ \"ЦЕНТР ХОЗЯЙСТВЕННОГО И СЕРВИСНОГО ОБЕСПЕЧЕНИЯ ГЛАВНОГО УПРАВЛЕНИЯ МИНИСТЕРСТВА ВНУТРЕННИХ ДЕЛ РОССИЙСКОЙ ФЕДЕРАЦИИ ПО ИРКУТСКОЙ ОБЛАСТИ\"";
    private static final String TEST_RESCTRICTIONS = "1 Единые требования к участникам закупок в соответствии с ч. 1 ст. 31 Закона № 44-ФЗ дополнительная информация к требованию отсутствует " +
            "2 Требования к участникам закупок в соответствии с частью 1.1 статьи 31 Федерального закона № 44-ФЗ дополнительная информация к требованию отсутствует";
    private static final String TEST_LINK_ON_PLACEMENT = "http://roseltorg.ru";
    private static final String TEST_APPLICATION_SECURE = "28098.84";
    private static final String TEST_CONTRACT_SECURE = "140494.20(5%)";
    private static final String TEST_OBJECT_OF = "Поставка шин пневматических для легковых автомобилей (Запасные части для ремонта к автотранспорту в рамках ГОЗ, в целях обеспечения ГПВ)";
    private static final String TEST_APPLICATION_DEADLINE = "30.08.2021 09:00";
    private static final String TEST_TIME_ZONE = "UTC+8";
    private static final String TEST_LAST_UPDATED_FROM_EIS = "20.08.2021";
    private static final String TEST_DATE_LAST_UPDATED = "20.08.2021";
    private static final String DATE_TIME_FORMATTER = "dd.MM.yyyy HH:mm";
    private static final String DATE_FORMATTER = "dd.MM.yyyy";
    private static final String ENCODING = "UTF-8";

    private ZakupkiParser44Fz parser44Fz;
    private Document goodHTML;
    private Document badHTML;

    @BeforeEach
    void setUp() throws URISyntaxException, IOException {
        parser44Fz = new ZakupkiParser44Fz();
        goodHTML = Jsoup.parse(new File(this.getClass().getClassLoader().getResource(FILE_NAME_GOOD_HTML).toURI()), ENCODING);
        badHTML = Jsoup.parse(new File(this.getClass().getClassLoader().getResource(FILE_NAME_BAD_HTML).toURI()), ENCODING);
    }

    @Test
    void givenHtml_whenGetStage_getStage() {
        Stage stage = Stage.get(TEST_STAGE);
        Assertions.assertEquals(stage, parser44Fz.getStage(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetStage_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getStage(badHTML));
    }

    @Test
    void givenHtml_whenGetUin_getUin() {
        Assertions.assertEquals(TEST_UIN, parser44Fz.getUin(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetUin_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getUin(badHTML));
    }

    @Test
    void givenHtml_whenGetFzNumber_getFzNumber() {
        Assertions.assertEquals(TEST_FZ_NUMBER, parser44Fz.getFzNumber(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetFzNumber_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getFzNumber(badHTML));
    }

    @Test
    void givenHtml_whenGetContractPrice_getContractPrice() {
        BigDecimal contractPrice = new BigDecimal(TEST_CONTRACT_PRICE);
        Assertions.assertEquals(contractPrice, parser44Fz.getContractPrice(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetContractPrice_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getContractPrice(badHTML));
    }

    @Test
    void givenHtml_whenGetProcedureType_getProcedureType() {
        ProcedureType procedureType = ProcedureType.get(TEST_PROCEDURE_TYPE);
        Assertions.assertEquals(procedureType, parser44Fz.getProcedureType(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetProcedureType_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getProcedureType(badHTML));
    }

    @Test
    void givenHtml_whenGetPublisherName_getPublisherName() {
        Assertions.assertEquals(TEST_PUBLISHER_NAME, parser44Fz.getPublisherName(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetPublisherName_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getPublisherName(badHTML));
    }

    @Test
    void givenHtml_whenGetRestrictions_getRestrictions() {
        Assertions.assertEquals(TEST_RESCTRICTIONS, parser44Fz.getRestrictions(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetRestrictions_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getRestrictions(badHTML));
    }

    @Test
    void givenHtml_whenGetLinkOnPlacement_getLinkOnPlacement() {
        Assertions.assertEquals(TEST_LINK_ON_PLACEMENT, parser44Fz.getLinkOnPlacement(goodHTML).toString());
    }

    @Test
    void givenBadHtml_whenGetLinkOnPlacement_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getLinkOnPlacement(badHTML));
    }

    @Test
    void givenHtml_whenGetApplicationSecure_getApplicationSecure() {
        Assertions.assertEquals(TEST_APPLICATION_SECURE, parser44Fz.getApplicationSecure(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetApplicationSecure_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getApplicationSecure(badHTML));
    }

    @Test
    void givenHtml_whenGetContractSecure_getContractSecure() {
        Assertions.assertEquals(TEST_CONTRACT_SECURE, parser44Fz.getContractSecure(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetContractSecure_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getContractSecure(badHTML));
    }

    @Test
    void givenHtml_whenGetObjectOf_getObjectOf() {
        Assertions.assertEquals(TEST_OBJECT_OF, parser44Fz.getObjectOf(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetObjectOf_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getApplicationDeadline(badHTML));
    }

    @Test
    void givenHtml_whenGetApplicationDeadline_getApplicationDeadline() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.parse(TEST_APPLICATION_DEADLINE, format), ZoneId.of(TEST_TIME_ZONE));
        Assertions.assertEquals(zonedDateTime, parser44Fz.getApplicationDeadline(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetApplicationDead_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getApplicationDeadline(badHTML));
    }

    @Test
    void givenHtml_whenGetLastUpdatedFromEIS_getLastUpdatedFromEIS() {
        LocalDate tempLd = LocalDate.parse(TEST_LAST_UPDATED_FROM_EIS, DateTimeFormatter.ofPattern(DATE_FORMATTER));
        Assertions.assertEquals(tempLd, parser44Fz.getLastUpdatedFromEIS(goodHTML));

    }

    @Test
    void givenBadHtml_whenGetLastUpdatedFromEIS_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getLastUpdatedFromEIS(badHTML));
    }

    @Test
    void givenHtml_whenGetDateTimeLastUpdated_getDateTimeLastUpdated() {
        LocalDate tempLd = LocalDate.parse(TEST_DATE_LAST_UPDATED, DateTimeFormatter.ofPattern(DATE_FORMATTER));
        Assertions.assertEquals(tempLd, parser44Fz.getDateTimeLastUpdated(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetDateTimeLastUpdated_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getDateTimeLastUpdated(badHTML));
    }

    @Test
    void givenHtml_whenGetTimeZone_getTimeZone() {
        Assertions.assertEquals(TEST_TIME_ZONE, parser44Fz.getTimeZone(goodHTML));
    }

    @Test
    void givenBadHtml_whenGetTimeZone_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser44Fz.getTimeZone(badHTML));
    }
}
