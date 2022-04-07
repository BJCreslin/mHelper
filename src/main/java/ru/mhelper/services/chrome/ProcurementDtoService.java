package ru.mhelper.services.chrome;

import org.springframework.security.core.userdetails.UserDetails;
import ru.mhelper.models.dto.ProcurementDto;
import ru.mhelper.models.users.User;

public interface ProcurementDtoService {

    void save(ProcurementDto procurementDto);

    void save(UserDetails user, ProcurementDto procurementDto);
}
