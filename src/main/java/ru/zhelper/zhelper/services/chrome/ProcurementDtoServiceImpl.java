package ru.zhelper.zhelper.services.chrome;

import ru.zhelper.zhelper.models.dto.ProcurementDto;
import ru.zhelper.zhelper.services.dao.ProcurementDataManager;

public class ProcurementDtoServiceImpl implements ProcurementDtoService {
    private final ProcurementDataManager procurementDataManager;

    public ProcurementDtoServiceImpl(ProcurementDataManager procurementDataManager) {
        this.procurementDataManager = procurementDataManager;
    }

    public void save(ProcurementDto procurementDto) {
        var procurement = changeDtoToOriginal(procurementDto);
        if (procurementDataManager.)
    }
}
