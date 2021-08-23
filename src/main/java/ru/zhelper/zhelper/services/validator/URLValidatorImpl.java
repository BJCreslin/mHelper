package ru.zhelper.zhelper.services.validator;

import org.springframework.stereotype.Service;
import ru.zhelper.zhelper.models.ProcurementType;
import ru.zhelper.zhelper.services.validator.URLValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class URLValidatorImpl implements URLValidator {
    @Override
    public boolean isValidUrl(String url, ProcurementType procurementType) {

        boolean isValid;
        String regex = "";

        switch (procurementType) {
            case LAW_44:
                regex = "^(https://zakupki.gov.ru/epz/order/notice/)(ea44|zk20|ep44|inm111|ok504|za44|zp504|po44|oku504|zk44|ok44" +
                        "|zkk44|okd504|zkku44|oku44|okd44|zkb44|zp44|ezt20)(/view/common-info.html)\\?(regNumber=)\\d{18,19}.";
                break;
            case LAW_223:
                regex = "^(https://zakupki.gov.ru/223/purchase/public/purchase/info/common-info.html)\\?(regNumber=)\\d{10,11}.";
                break;
            case LAW_615:
                regex = "^(https://zakupki.gov.ru/epz/order/notice/)(po615|ea615)(/view/common-info.html)\\?(regNumber=)\\d{17,18}.";
                break;
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        isValid = matcher.matches();

        return isValid;

    }

}
