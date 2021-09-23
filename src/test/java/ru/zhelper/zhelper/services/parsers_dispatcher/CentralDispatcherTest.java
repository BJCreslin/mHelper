package ru.zhelper.zhelper.services.parsers_dispatcher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.zhelper.zhelper.models.Procurement;
import ru.zhelper.zhelper.models.ProcurementType;
import ru.zhelper.zhelper.services.exceptions.BadDataParsingException;
import ru.zhelper.zhelper.services.exceptions.BadRequest;
import ru.zhelper.zhelper.services.parsers_dispatcher.parser.ZakupkiParser;
import ru.zhelper.zhelper.services.validator.URLValidator;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class CentralDispatcherTest {
    private static final String LAW_44_PARSER = "Law44";
    private static final String LAW_615_PARSER = "Law615";
    private static final String TEST_THROW = "Test throw";
    @Mock
    private URLValidator validator;
    @Mock
    private ZakupkiParser parser;

    private Dispatcher dispatcher;

    @ParameterizedTest
    @ValueSource(strings = {
            "https://zakupki.gov.ru/epz/order/notice/ea44/view/common-info.html?regNumber=0338200000421000026",
            "https://zakupki.gov.ru/epz/order/notice/ep44/view/common-info.html?regNumber=0518600019917000001",
            "https://zakupki.gov.ru/epz/order/notice/zk20/view/common-info.html?regNumber=0361200015021004947",
            "https://zakupki.gov.ru/epz/order/notice/inm111/view/common-info.html?regNumber=0338100009421000068",
            "https://zakupki.gov.ru/epz/order/notice/ok504/view/common-info.html?regNumber=0347200007521000114",
            "https://zakupki.gov.ru/epz/order/notice/ok504/view/common-info.html?regNumber=0161300001921000053",
            "https://zakupki.gov.ru/epz/order/notice/zp504/view/common-info.html?regNumber=0161300000121000725"
    })
    void givenUrlWith44fz_thenGetFromUrl_getProcurement(String url) {
        Procurement procurement = new Procurement();
        Mockito.when(validator.isValidUrl(url)).thenReturn(true);
        Mockito.when(validator.getProcurementType(url)).thenReturn(ProcurementType.LAW_44);
        Mockito.when(parser.parse(url)).thenReturn(procurement);
        Map<String, ZakupkiParser> parsers = new HashMap<>();
        parsers.put(LAW_44_PARSER, parser);
        parsers.put(LAW_615_PARSER, parser);
        dispatcher = new CentralDispatcher(parsers, validator);
        Procurement procurementFromDispatcher = dispatcher.getFromUrl(url);
        Mockito.verify(validator).isValidUrl(url);
        Mockito.verify(validator).getProcurementType(url);
        Mockito.verify(parser).parse(url);
        Assertions.assertEquals(procurement, procurementFromDispatcher);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://zakupki.gov.ru/epz/order/notice/ea615/view/common-info.html?regNumber=203950000012101127",
            "https://zakupki.gov.ru/epz/order/notice/po615/view/common-info.html?regNumber=011020000322100015"
    })
    void givenUrlWith615fz_thenGetFromUrl_getProcurement(String url) {
        Procurement procurement = new Procurement();
        Mockito.when(validator.isValidUrl(url)).thenReturn(true);
        Mockito.when(validator.getProcurementType(url)).thenReturn(ProcurementType.LAW_615);
        Mockito.when(parser.parse(url)).thenReturn(procurement);
        Map<String, ZakupkiParser> parsers = new HashMap<>();
        parsers.put(LAW_44_PARSER, parser);
        parsers.put(LAW_615_PARSER, parser);
        dispatcher = new CentralDispatcher(parsers, validator);
        Procurement procurementFromDispatcher = dispatcher.getFromUrl(url);
        Mockito.verify(validator).isValidUrl(url);
        Mockito.verify(validator).getProcurementType(url);
        Mockito.verify(parser).parse(url);
        Assertions.assertEquals(procurement, procurementFromDispatcher);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://zakupki.gov.ru/epz/order/notice/ea615/view/common-info.html?regNumber=203950000012101127",
            "https://zakupki.gov.ru/epz/order/notice/po615/view/common-info.html?regNumber=011020000322100015"
    })
    void givenUrlWithBadProcurement_thenGetFromUrl_getException(String url) {
        Mockito.when(validator.isValidUrl(url)).thenReturn(true);
        Mockito.when(validator.getProcurementType(url)).thenReturn(ProcurementType.LAW_615);
        Mockito.when(parser.parse(url)).thenThrow(new BadDataParsingException(TEST_THROW, null));
        Map<String, ZakupkiParser> parsers = new HashMap<>();
        parsers.put(LAW_44_PARSER, parser);
        parsers.put(LAW_615_PARSER, parser);
        dispatcher = new CentralDispatcher(parsers, validator);
        Exception exception = assertThrows(BadRequest.class, () -> {
            dispatcher.getFromUrl(url);
        });
        assertEquals(BadRequest.class, exception.getClass());
        Mockito.verify(validator).isValidUrl(url);
        Mockito.verify(validator).getProcurementType(url);
        Mockito.verify(parser).parse(url);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://zakupki.gov.ru/epz/order/notice/ea615/view/common-info.html?regNumber=203950000012101127",
            "https://zakupki.gov.ru/epz/order/notice/po615/view/common-info.html?regNumber=011020000322100015"
    })
    void givenUrlWithBadUrl_thenGetFromUrl_getException(String url) {
        Mockito.when(validator.isValidUrl(url)).thenReturn(false);
        Mockito.when(validator.getProcurementType(url)).thenReturn(ProcurementType.LAW_615);
        Mockito.when(parser.parse(url)).thenThrow(new BadDataParsingException(TEST_THROW, null));
        Map<String, ZakupkiParser> parsers = new HashMap<>();
        parsers.put(LAW_44_PARSER, parser);
        parsers.put(LAW_615_PARSER, parser);
        dispatcher = new CentralDispatcher(parsers, validator);
        Exception exception = assertThrows(BadRequest.class, () -> {
            dispatcher.getFromUrl(url);
        });
        assertEquals(BadRequest.class, exception.getClass());
        Mockito.verify(validator).isValidUrl(url);
        Mockito.verify(validator, Mockito.times(0)).getProcurementType(url);
        Mockito.verify(parser, Mockito.times(0)).parse(url);
    }

}
