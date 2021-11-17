package ru.zhelper.zhelper.services.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.zhelper.zhelper.models.procurements.ProcurementType;

import java.util.ArrayList;

class URLValidatorImplTest {

    private static final ArrayList<String> testURLs = new ArrayList<>();

    static {
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/ea44/view/common-info.html?regNumber=0547600000521000006");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/ep44/view/common-info.html?regNumber=0518600019917000001");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/zk20/view/common-info.html?regNumber=0323300084121000065");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/inm111/view/common-info.html?regNumber=0161000000221000124");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/ok504/view/common-info.html?regNumber=0816500000621009899");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/za44/view/common-info.html?regNumber=0851100001621000152");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/zp504/view/common-info.html?regNumber=0161200002921000001");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/po44/view/common-info.html?regNumber=0152300011620000002");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/oku504/view/common-info.html?regNumber=0516100000121000009");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/zk44/view/common-info.html?regNumber=0311400002421000065");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/ok44/view/common-info.html?regNumber=0534200000221000001");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/zkk44/view/common-info.html?regNumber=0140100008521000024");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/okd504/view/common-info.html?regNumber=0161300000121000400");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/zkku44/view/common-info.html?regNumber=0375100002621000062");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/oku44/view/common-info.html?regNumber=0129300014514000118");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/okd44/view/common-info.html?regNumber=0162300005718000147");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/zkb44/view/common-info.html?regNumber=0322200000820000025");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/zp44/view/common-info.html?regNumber=0306100013114000018");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/ezt20/view/common-info.html?regNumber=0373200658821000003");
        testURLs.add("https://zakupki.gov.ru/223/purchase/public/purchase/info/common-info.html?regNumber=32110575951");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/po615/view/common-info.html?regNumber=015450000042100007");
        testURLs.add("https://zakupki.gov.ru/epz/order/notice/ea615/view/common-info.html?regNumber=202320000012100328");
    }

    @Test
    void givenURL_whenIsValid_thenTrue() {
        boolean result = false;
        URLValidatorImpl urlValidator = new URLValidatorImpl();

        for(String testURL : testURLs) {
            result = urlValidator.isValidUrl(testURL);
            if(!result) {
                break;
            }
        }
        Assertions.assertTrue(result);
    }

    @Test
    void givenURL_whenIsValid_thenProcurementType() {
        boolean result = false;
        ProcurementType procurementType;
        URLValidatorImpl urlValidator = new URLValidatorImpl();

        for(String testURL : testURLs) {
            procurementType = urlValidator.getProcurementType(testURL);
            if(procurementType == null) {
                result = false;
                break;
            } else {
                result = true;
            }
        }
        Assertions.assertTrue(result);
    }

    @Test
    void givenURL_whenNull_thenFalse() {
        boolean result;
        ProcurementType procurementType;
        URLValidatorImpl urlValidator = new URLValidatorImpl();
        result = urlValidator.isValidUrl(null);
        Assertions.assertFalse(result);
    }

    @Test
    void givenURL_whenIsNotValid_thenFalse() {
        boolean result = false;
        ProcurementType procurementType;
        URLValidatorImpl urlValidator = new URLValidatorImpl();

        procurementType = urlValidator.getProcurementType("https://zakupki.gov.ru/ddd/order/notice/ea615/view/common-info.html?regNumber=202320000012100328");
        if(procurementType == null) {
                result = true;
            } 

        Assertions.assertTrue(result);
    }

    @Test
    void givenURL_whenIsValid_thenReturnProcurementType() {
        ProcurementType procurementType;
        URLValidatorImpl urlValidator = new URLValidatorImpl();

        procurementType = urlValidator.getProcurementType("https://zakupki.gov.ru/epz/order/notice/ea44/view/common-info.html?regNumber=0547600000521000006");
        Assertions.assertEquals("LAW_44", procurementType.name());

        procurementType = urlValidator.getProcurementType("https://zakupki.gov.ru/223/purchase/public/purchase/info/common-info.html?regNumber=32110575951");
        Assertions.assertEquals("LAW_223", procurementType.name());

        procurementType = urlValidator.getProcurementType("https://zakupki.gov.ru/epz/order/notice/po615/view/common-info.html?regNumber=015450000042100007");
        Assertions.assertEquals("LAW_615", procurementType.name());
    }
}
