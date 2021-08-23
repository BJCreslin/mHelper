package ru.zhelper.zhelper.services;

import org.springframework.stereotype.Repository;

import ru.zhelper.zhelper.models.Procurement;

@Repository
public interface ProcurementDataManager {
    
    Procurement loadProcurement(Long id);
    
    Procurement saveProcurement(Procurement p);
    
    void deleteProcurement(Procurement p);
    
    void deleteProcurementById(Long idToDelete);
}
