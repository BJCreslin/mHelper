package ru.mhelper.services.procurement;

import ru.mhelper.models.dto.ProcurementAddress;
import ru.mhelper.models.procurements.Procurement;

import java.time.ZonedDateTime;
import java.util.List;

public interface ProcurementService {
    void action(ProcurementAddress procurementAddress);

}
