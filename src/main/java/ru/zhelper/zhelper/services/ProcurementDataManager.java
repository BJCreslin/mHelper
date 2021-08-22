package ru.zhelper.zhelper.services;

import ru.zhelper.zhelper.models.Procurement;

public interface ProcurementDataManager {
    
    Procurement loadProcurement(Long id);
    
    Procurement saveProcurement(Procurement p);
    
    void deleteProcurement(Procurement p);
    
    void deleteProcurementById(Long idToDelete);
}
