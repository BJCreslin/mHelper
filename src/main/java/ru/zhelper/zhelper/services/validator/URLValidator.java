package ru.zhelper.zhelper.services.validator;

import ru.zhelper.zhelper.models.ProcurementType;

public interface URLValidator {
    boolean isValidUrl (String url, ProcurementType procurementType);
}
