package ru.zhelper.zhelper.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ru.zhelper.zhelper.models.Procurement;

public interface ProcurementDataManager {
    
    Procurement loadById(Long id);
    
    Procurement save(Procurement procurement);
    
    void delete(Procurement procurement);
    
    void deleteById(Long idToDelete);

	List<Procurement> loadListByFzNumber(Integer fzNumber);
	
	Page<Procurement> loadAll(Pageable pageable);

	Page<Procurement> loadByIdList(List<Long> idsToLoad, Pageable pageable);

	Page<Procurement> loadCreatedBeforeDate(LocalDate date, Pageable pageable);
}
