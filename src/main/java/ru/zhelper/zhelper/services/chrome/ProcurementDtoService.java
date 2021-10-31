package ru.zhelper.zhelper.services.chrome;

import org.springframework.stereotype.Service;
import ru.zhelper.zhelper.models.dto.ProcurementDto;

@Service
public interface ProcurementDtoService {

    void save(ProcurementDto procurementDto);
}
