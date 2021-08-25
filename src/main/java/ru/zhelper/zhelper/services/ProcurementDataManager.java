package ru.zhelper.zhelper.services;

import java.util.List;

import org.springframework.stereotype.Repository;

import ru.zhelper.zhelper.models.Procurement;

@Repository
public interface ProcurementDataManager {
    
    Procurement loadEntity(Long id);
    
    Procurement saveEntity(Procurement p);
    
    void deleteEntity(Procurement p);
    
    void deleteEntityById(Long idToDelete);

	List<Procurement> getProcurementsByFzNumber(Integer fzNumber);
}
