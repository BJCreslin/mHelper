package ru.zhelper.zhelper.services.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.zhelper.zhelper.models.ProcurementType;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class URLValidatorImplTest {

    private static Map<String, ProcurementType> testURLs = new ConcurrentHashMap<>();

    static {
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/ea44/view/common-info.html?regNumber=0547600000521000006", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/ep44/view/common-info.html?regNumber=0518600019917000001", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/zk20/view/common-info.html?regNumber=0323300084121000065", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/inm111/view/common-info.html?regNumber=0161000000221000124", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/ok504/view/common-info.html?regNumber=0816500000621009899", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/za44/view/common-info.html?regNumber=0851100001621000152", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/zp504/view/common-info.html?regNumber=0161200002921000001", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/po44/view/common-info.html?regNumber=0152300011620000002", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/oku504/view/common-info.html?regNumber=0516100000121000009", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/zk44/view/common-info.html?regNumber=0311400002421000065", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/ok44/view/common-info.html?regNumber=0534200000221000001", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/zkk44/view/common-info.html?regNumber=0140100008521000024", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/okd504/view/common-info.html?regNumber=0161300000121000400", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/zkku44/view/common-info.html?regNumber=0375100002621000062", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/oku44/view/common-info.html?regNumber=0129300014514000118", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/okd44/view/common-info.html?regNumber=0162300005718000147", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/zkb44/view/common-info.html?regNumber=0322200000820000025", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/zp44/view/common-info.html?regNumber=0306100013114000018", ProcurementType.LAW_44);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/ezt20/view/common-info.html?regNumber=0373200658821000003", ProcurementType.LAW_44);

        testURLs.put("https://zakupki.gov.ru/223/purchase/public/purchase/info/common-info.html?regNumber=32110575951", ProcurementType.LAW_223);

        testURLs.put("https://zakupki.gov.ru/epz/order/notice/po615/view/common-info.html?regNumber=015450000042100007", ProcurementType.LAW_615);
        testURLs.put("https://zakupki.gov.ru/epz/order/notice/ea615/view/common-info.html?regNumber=202320000012100328", ProcurementType.LAW_615);

    }


    @Test
    void isValidUrlTest() {
        boolean result = false;
        URLValidatorImpl urlValidator = new URLValidatorImpl();

        for(Map.Entry<String, ProcurementType> testURL : testURLs.entrySet()) {

            result = urlValidator.isValidUrl(testURL.getKey(), testURL.getValue());

            if(!result) {
                break;
            }
        }
        Assertions.assertTrue(result);
    }

}