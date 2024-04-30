package ru.thelper.services.validator;

import ru.thelper.models.procurements.ProcurementType;

public interface URLValidator {
    boolean isValidUrl (String url);
    ProcurementType getProcurementType (String url);
}
