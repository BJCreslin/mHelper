package ru.zhelper.zhelper.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.zhelper.zhelper.models.Stage;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ZakupkiParser44FzTest {
    private static final String UIN = "0834100000221000038";
    private static final String FZ_NUMBER = "44";
    private static final BigDecimal CONTRACT_PRICE = new BigDecimal(2809884.00);
    private static final String FILE_NAME_GOOD_HTML = "44fz0834100000221000038.html";
    private static final String FILE_NAME_BAD_HTML = "44fz0834100000221000038bad.html";
    ZonedDateTime applicationDeadline; //"2021-08-30T09:00+08:00[UTC+08:00]"
    String contractPrice = "2809884.00"; //to BigDecimal
    String procedureType = "Электронный аукцион"; //to ProcedureType
    String publisherName = "ФЕДЕРАЛЬНОЕ КАЗЕННОЕ УЧРЕЖДЕНИЕ \"ЦЕНТР ХОЗЯЙСТВЕННОГО И СЕРВИСНОГО ОБЕСПЕЧЕНИЯ ГЛАВНОГО УПРАВЛЕНИЯ МИНИСТЕРСТВА ВНУТРЕННИХ ДЕЛ РОССИЙСКОЙ ФЕДЕРАЦИИ ПО ИРКУТСКОЙ ОБЛАСТИ\"";
    String restrictions = "1 Единые требования к участникам закупок в соответствии с ч. 1 ст. 31 Закона № 44-ФЗ дополнительная информация к требованию отсутствует \n" +
            "2 Требования к участникам закупок в соответствии с частью 1.1 статьи 31 Федерального закона № 44-ФЗ дополнительная информация к требованию отсутствует";
    String linkOnPlacement = "http://roseltorg.ru"; //to URL
    String applicationSecure = "28098.84";
    String contractSecure = "140494.20(5%)";
    String objectOf = "Поставка шин пневматических для легковых автомобилей (Запасные части для ремонта к автотранспорту в рамках ГОЗ, в целях обеспечения ГПВ)";
    LocalDate lastUpdatedFromEIS; //"2021-08-20"
    LocalDate dateTimeLastUpdated; //"2021-08-20"

    private ZakupkiParser44Fz parser44Fz;
    private Document goodHTML;
    private Document badHTML;

    @BeforeEach
    void setUp() throws URISyntaxException, IOException {
        parser44Fz = new ZakupkiParser44Fz();
        //goodHTML = Jsoup.parse(new File(this.getClass().getClassLoader().getResource(FILE_NAME_GOOD_HTML).toURI()), null);
        //badHTML = Jsoup.parse(new File(this.getClass().getClassLoader().getResource(FILE_NAME_BAD_HTML).toURI()), null);
    }

    @Test
    void parse() {
    }

    @Test
    void getStage() {
        //Stage stageFirst = Stage.get("Подача заявок");
    }

    @Test
    void getUin() {
    }

    @Test
    void getFzNumber() {
    }

    @Test
    void getContractPrice() {
    }

    @Test
    void getProcedureType() {
    }

    @Test
    void getPublisherName() {
    }

    @Test
    void getRestrictions() {
    }

    @Test
    void getLinkOnPlacement() {
    }

    @Test
    void getApplicationSecure() {
    }

    @Test
    void getContractSecure() {
    }

    @Test
    void getObjectOf() {
    }

    @Test
    void getApplicationDeadline() {
    }

    @Test
    void getLastUpdatedFromEIS() {
    }

    @Test
    void getDateTimeLastUpdated() {
    }

    @Test
    void getTimeZone() {
    }
}