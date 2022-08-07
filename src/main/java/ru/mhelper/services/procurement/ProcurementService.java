package ru.mhelper.services.procurement;

import ru.mhelper.models.dto.ProcurementAddress;
import ru.mhelper.models.procurements.Procurement;

import java.time.LocalDateTime;
import java.util.List;

public interface ProcurementService {
    void action(ProcurementAddress procurementAddress);

    List<Procurement> getAllBeforeTime(LocalDateTime nextTimeEvent);
}
