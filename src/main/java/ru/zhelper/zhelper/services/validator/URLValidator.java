package ru.zhelper.zhelper.services.validator;

import ru.zhelper.zhelper.models.procurements.ProcurementType;

public interface URLValidator {
    boolean isValidUrl (String url);
    ProcurementType getProcurementType (String url);
}
