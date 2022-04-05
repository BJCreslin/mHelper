package ru.mhelper.services.chrome;

import ru.mhelper.models.dto.ProcurementDto;
import ru.mhelper.models.users.User;

public interface ProcurementDtoService {

    void save(ProcurementDto procurementDto);

    void save(User user, ProcurementDto procurementDto);
}
