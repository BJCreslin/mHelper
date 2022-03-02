package ru.mhelper.services.validator;

import org.springframework.stereotype.Service;
import ru.mhelper.models.procurements.ProcurementType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class URLValidatorImpl implements URLValidator {

    private static final Map<String, ProcurementType> regexProcurementType = new ConcurrentHashMap<>();

    static {
        regexProcurementType.put("^(https://zakupki.gov.ru/epz/order/notice/)(ea44|zk20|ep44|inm111|ok504|za44|zp504|po44|oku504|zk44|ok44" +
                "|zkk44|okd504|zkku44|oku44|okd44|zkb44|zp44|ezt20)(/view/common-info.html)\\?(regNumber=)\\d{18,19}.", ProcurementType.LAW_44);
        regexProcurementType.put("^(https://zakupki.gov.ru/223/purchase/public/purchase/info/common-info.html)\\?(regNumber=)\\d{10,11}.", ProcurementType.LAW_223);
        regexProcurementType.put("^(https://zakupki.gov.ru/epz/order/notice/)(po615|ea615)(/view/common-info.html)\\?(regNumber=)\\d{17,18}.", ProcurementType.LAW_615);
    }

    @Override
    public boolean isValidUrl(String url) {

        boolean isValid = false;

        if(url == null) {
            return isValid;
        }

        for(Map.Entry<String, ProcurementType> regexURL : regexProcurementType.entrySet()) {
           Pattern pattern = Pattern.compile(regexURL.getKey());
           Matcher matcher = pattern.matcher(url);
            isValid = matcher.matches();
           if(isValid) {
               break;
           }
       }
        return isValid;
    }

    @Override
    public ProcurementType getProcurementType(String url) {
        ProcurementType procurementType;

        boolean isValid;

        procurementType = null;

        for(Map.Entry<String, ProcurementType> regexURL : regexProcurementType.entrySet()) {
            Pattern pattern = Pattern.compile(regexURL.getKey());
            Matcher matcher = pattern.matcher(url);
            isValid = matcher.matches();
            if(isValid) {
                procurementType = regexURL.getValue();
                break;
            }
        }
        return procurementType;
    }


}
