package ru.mhelper.services.chrome;

import org.springframework.security.core.userdetails.UserDetails;
import ru.mhelper.models.dto.ProcurementAddress;
import ru.mhelper.models.dto.ProcurementDto;

public interface ProcurementService {

    void save(ProcurementDto procurementDto);

    void save(UserDetails user, ProcurementDto procurementDto);

    void action(ProcurementAddress procurementAddress);
}
