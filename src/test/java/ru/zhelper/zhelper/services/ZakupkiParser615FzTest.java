package ru.zhelper.zhelper.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.zhelper.zhelper.models.ProcedureType;
import ru.zhelper.zhelper.models.Stage;
import ru.zhelper.zhelper.services.exceptions.BadDataParsingException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class ZakupkiParser615FzTest {
    private static final String DATE_TIME_FORMATTER = "dd.MM.yyyy HH:mm";
    private static final String fileName = "206520000012100111.html";
    private static final String fileBadName = "206520000012100111bad.html";
    private static final String UIN = "206520000012100111";
    private static final String PUBLISHER = "ФОНД \"РЕГИОНАЛЬНЫЙ ФОНД КАПИТАЛЬНОГО РЕМОНТА МНОГОКВАРТИРНЫХ ДОМОВ ТОМСКОЙ ОБЛАСТИ\"";
    private static final String RESTRICTION = "Оплата выполненных работ, включая форму, сроки и порядок оплаты работ, осуществляется в порядке, указанном в разделе XVII «Проект договора о выполнении капитального ремонта».";
    private static final String OBJECT_OF = "Выполнение работ по разработке проектно-сметной документации (включая проведение проверки достоверности определения сметной стоимости) на выполнение работ по капитальному ремонту общего имущества в многоквартирных домах, расположенных на территории Томской области по адресам: г. Стрежевой, 3-й микрорайон, д. 317; Томский район, п. Молодежный, д. 7; Томский район, п. Молодежный, д. 8 (переустройство невентилируемой крыши на вентилируемую крышу).";
    private static final String DEADLINE = "01.09.2021 23:59";
    private static final String APPLICATION_SECURE = "13055.24";
    private static final String CONTRACT_SECURE = "39165.72";
    private static URL LINK = null;

    static {
        try {
            LINK = new URL("http://www.rts-tender.ru");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static final BigDecimal CONTRACT_PRICE = new BigDecimal("1305523.99");
    private static final Integer FZ = 615;
    private ZakupkiParser615Fz parser;
    private String fineHtml;
    private String badHtml;

    @BeforeEach
    private void init() throws IOException, URISyntaxException {
        parser = new ZakupkiParser615Fz();
        fineHtml = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource(fileName).toURI())));
        badHtml = new String(Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource(fileBadName).toURI())));
    }

    @Test
    void givenHtml_whenGetUin_getUin() {
        String result = parser.getUin(fineHtml);
        Assertions.assertEquals(UIN, result);
    }

    @Test
    void givenBadHtml_whenGetUin_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getUin(badHtml));
    }

    @Test
    void givenHtml_whenGetStage_getStage() {
        Stage result = parser.getStage(fineHtml);
        Assertions.assertEquals(Stage.SUBMISSION_OF_APPLICATION, result);
    }

    @Test
    void givenBadHtml_whenGetStage_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getStage(badHtml));
    }

    @Test
    void givenHtml_whenGetUin_getFzNumber() {
        Integer result = parser.getFzNumber(fineHtml);
        Assertions.assertEquals(FZ, result);
    }

    @Test
    void givenBadHtml_whenGetUin_getFzNumber() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getFzNumber(badHtml));
    }

    @Test
    void givenHtml_whenGetApplicationDeadline_getDeadLine() {
        LocalDateTime result = parser.getApplicationDeadline(fineHtml);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER);
        Assertions.assertEquals(result, LocalDateTime.parse(DEADLINE, formatter));
    }

    @Test
    void givenBadHtml_whenGetApplicationDeadline_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getApplicationDeadline(badHtml));
    }

    @Test
    void givenHtml_whenGetContractPrice_getContractPrice() {
        BigDecimal result = parser.getContractPrice(fineHtml);
        Assertions.assertEquals(CONTRACT_PRICE, result);
    }

    @Test
    void givenBadHtml_whenGetContractPrice_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getContractPrice(badHtml));
    }

    @Test
    void givenHtml_whenGetProcedureType_getProcedureType() {
        ProcedureType result = parser.getProcedureType(fineHtml);
        Assertions.assertEquals(ProcedureType.ELECTRONIC_AUCTION_615FZ, result);
    }

    @Test
    void givenBadHtml_whenGetProcedureType_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getProcedureType(badHtml));
    }

    @Test
    void givenHtml_whenGetPublisherName_getPublisherName() {
        String result = parser.getPublisherName(fineHtml);
        Assertions.assertEquals(PUBLISHER, result);
    }

    @Test
    void givenBadHtml_whenGetPublisherName_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getPublisherName(badHtml));
    }

    @Test
    void givenHtml_whenGetRestrictions_getRestrictions() {
        String result = parser.getRestrictions(fineHtml);
        Assertions.assertEquals(RESTRICTION, result);
    }

    @Test
    void givenBadHtml_whenGetRestrictions_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getRestrictions(badHtml));
    }

    @Test
    void givenHtml_whenGetLinkOnPlacement_getLinkOnPlacement() {
        URL result = parser.getLinkOnPlacement(fineHtml);
        Assertions.assertEquals(LINK, result);
    }

    @Test
    void givenBadHtml_whenGetLinkOnPlacement_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getLinkOnPlacement(badHtml));
    }

    @Test
    void givenHtml_whenGetApplicationSecure_getApplicationSecure() {
        String result = parser.getApplicationSecure(fineHtml);
        Assertions.assertEquals(APPLICATION_SECURE, result);
    }

    @Test
    void givenBadHtml_whenGetApplicationSecure_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getApplicationSecure(badHtml));
    }

    @Test
    void givenHtml_whenGetContractSecure_getApplicationSecure() {
        String result = parser.getContractSecure(fineHtml);
        Assertions.assertEquals(CONTRACT_SECURE, result);
    }

    @Test
    void givenBadHtml_whenGetContractSecure_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getContractSecure(badHtml));
    }

    @Test
    void givenHtml_whenGetObjectOf_getObjectOf() {
        String result = parser.getObjectOf(fineHtml);
        Assertions.assertEquals(OBJECT_OF, result);
    }

    @Test
    void givenBadHtml_whenGetObjectOf_getException() {
        Assertions.assertThrows(BadDataParsingException.class,
                () -> parser.getObjectOf(badHtml));
    }
}
