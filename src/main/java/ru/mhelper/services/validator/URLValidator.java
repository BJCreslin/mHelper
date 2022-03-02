package ru.mhelper.services.validator;

import ru.mhelper.models.procurements.ProcurementType;

public interface URLValidator {
    boolean isValidUrl (String url);
    ProcurementType getProcurementType (String url);
}
